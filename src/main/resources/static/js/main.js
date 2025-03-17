const inputBox = document.getElementById("input-box");
const taskTime = document.getElementById("task-time");
const notificationToggle = document.getElementById("notification-toggle");
const notificationTime = document.getElementById("notification-time");
const listContainer = document.getElementById("list-container");

// 드래그 앤 드롭 이벤트 처리
let draggedItem = null;

function handleDragStart(e) {
    draggedItem = this;
    this.classList.add('dragging');
    e.dataTransfer.effectAllowed = 'move';
    e.dataTransfer.setData('text/html', this.innerHTML);
}

function handleDragEnd(e) {
    this.classList.remove('dragging');
    draggedItem = null;
    saveData();
}

function handleDragOver(e) {
    e.preventDefault();
    return false;
}

function handleDragEnter(e) {
    e.preventDefault();
    this.classList.add('drag-over');
}

function handleDragLeave(e) {
    this.classList.remove('drag-over');
}

function handleDrop(e) {
    e.stopPropagation();
    e.preventDefault();
    
    if (draggedItem !== this) {
        const allItems = [...listContainer.querySelectorAll('li')];
        const draggedIndex = allItems.indexOf(draggedItem);
        const droppedIndex = allItems.indexOf(this);

        if (draggedIndex < droppedIndex) {
            this.parentNode.insertBefore(draggedItem, this.nextSibling);
        } else {
            this.parentNode.insertBefore(draggedItem, this);
        }
        
        // 드래그 앤 드롭 후에도 완료된 항목이 아래로 가도록 정렬
        setTimeout(sortTasks, 0);
    }
    
    this.classList.remove('drag-over');
    return false;
}

function addDragEvents(li) {
    li.draggable = true;
    li.addEventListener('dragstart', handleDragStart);
    li.addEventListener('dragend', handleDragEnd);
    li.addEventListener('dragover', handleDragOver);
    li.addEventListener('dragenter', handleDragEnter);
    li.addEventListener('dragleave', handleDragLeave);
    li.addEventListener('drop', handleDrop);
}

// 알림 토글 이벤트 처리
notificationToggle.addEventListener("change", function() {
    notificationTime.disabled = !this.checked;
});

function formatDateDiff(date) {
    const now = new Date();
    const diffTime = date - now;
    const diffDays = Math.ceil(diffTime / (1000 * 60 * 60 * 24));
    
    if (diffDays < 0) {
        return `${Math.abs(diffDays)}일 지남`;
    } else if (diffDays === 0) {
        return "오늘";
    } else {
        return `${diffDays}일 남음`;
    }
}

function sortTasks() {
    const tasks = [...listContainer.querySelectorAll('li')];
    tasks.sort((a, b) => {
        const aIsChecked = a.classList.contains('checked');
        const bIsChecked = b.classList.contains('checked');
        
        if (aIsChecked === bIsChecked) {
            // 둘 다 완료되었거나 둘 다 완료되지 않은 경우 날짜순 정렬
            const aDate = new Date(a.querySelector('.task-date').textContent.split(' ')[0]);
            const bDate = new Date(b.querySelector('.task-date').textContent.split(' ')[0]);
            return aDate - bDate;
        }
        
        // 완료된 항목은 아래로
        return aIsChecked ? 1 : -1;
    });
    
    // 정렬된 순서대로 DOM 재배치
    tasks.forEach(task => listContainer.appendChild(task));
}


function addTask() {
    if (inputBox.value === "") {
        alert("할일을 입력해주세요!");
        return;
    }
    if (taskTime.value === "") {
        alert("시간을 선택해주세요!");
        return;
    }

    const taskDate = new Date(taskTime.value);
    let li = document.createElement("li");

    let taskInfo = {
        content: inputBox.value,
        time: taskTime.value,
        notification: notificationToggle.checked,
        notificationTime: notificationToggle.checked ? notificationTime.value : null,
    };
    
    // 날짜가 지났는지 확인
    const now = new Date();
    const isPast = taskDate < now;
    
    li.innerHTML = `
        <div class="task-date ${isPast ? 'past-task' : 'future-task'}">
            ${taskDate.toLocaleDateString()} (${formatDateDiff(taskDate)})
        </div>
        <div class="task-content">
            ${taskInfo.content} - ${taskDate.toLocaleTimeString()}
            ${taskInfo.notification ? ` (${notificationTime.options[notificationTime.selectedIndex].text} 알림)` : ''}
        </div>
    `;
    
    // 드래그 이벤트 추가
    addDragEvents(li);
    
    listContainer.appendChild(li);
    sortTasks(); // 추가 후 정렬
    
    let span = document.createElement("span");
    span.innerHTML = "\u00d7";
    li.appendChild(span);

    // 알림 설정
    if (taskInfo.notification) {
        const notificationMinutes = parseInt(taskInfo.notificationTime);
        const notificationTime = new Date(taskDate.getTime() - (notificationMinutes * 60000));
        
        if (notificationTime > now) {
            setTimeout(() => {
                if (Notification.permission === "granted") {
                    new Notification("할일 알림", {
                        body: taskInfo.content,
                        icon: "/image/tasket-logo-black.png"
                    });
                } else if (Notification.permission !== "denied") {
                    Notification.requestPermission().then(permission => {
                        if (permission === "granted") {
                            new Notification("할일 알림", {
                                body: taskInfo.content,
                                icon: "/image/tasket-logo-black.png"
                            });
                        }
                    });
                }
            }, notificationTime - now);
        }
    }

    inputBox.value = "";
    taskTime.value = "";
    notificationToggle.checked = false;
    notificationTime.disabled = true;
    saveData();
}

// 기존 할일 목록에 드래그 이벤트 추가
function addDragEventsToExistingTasks() {
    const tasks = listContainer.querySelectorAll('li');
    tasks.forEach(task => addDragEvents(task));
}

listContainer.addEventListener("click", function(e) {
    if (e.target.tagName === "LI") {
        e.target.classList.toggle("checked");
        sortTasks(); // 체크 상태 변경 후 정렬
        saveData();
    } else if (e.target.tagName === "SPAN") {
        e.target.parentElement.remove();
        saveData();
    }
}, false);

function saveData() {
    localStorage.setItem("data", listContainer.innerHTML);
}

function showTask() {
    listContainer.innerHTML = localStorage.getItem("data") || "";
    addDragEventsToExistingTasks();
    sortTasks(); // 초기 로드 시 정렬
}

// 페이지 로드 시 할일 목록 표시
showTask();

// 카테고리 데이터 불러오기
loadCategories();

// 알림 권한 요청
if (Notification.permission !== "granted" && Notification.permission !== "denied") {
    Notification.requestPermission();
}
