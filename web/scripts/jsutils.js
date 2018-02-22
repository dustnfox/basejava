function addEmptyListItem(parentId) {
    addListItem(parentId, "");
}

function addListItem(parentId, value) {
    const list = document.getElementById(parentId);
    const counter = list.getElementsByTagName("input")[0];
    const index = parseInt(counter.value);
    const childId = parentId + "_item";
    counter.value = (index + 1).toString();
    list.insertAdjacentHTML("beforeend", `
        <li>
            <input size="30" name="${childId}" type="text" value="${value}">
        </li>`);
}

function addEmptyOrganization(parentId) {
    addOrganization(parentId, "", "");
}

function addOrganization(parentId, name, url) {
    const list = document.getElementById(parentId);
    const counter = list.getElementsByTagName("input")[0];
    const index = parseInt(counter.value);
    const childId = parentId + "_" + index;
    counter.value = (index + 1).toString();
    list.insertAdjacentHTML("beforeend", `
        <li>
            <table>
               <tr>
                    <td>Наименование: </td>
                    <td><input name="${childId}_name" type="text" value="${name}"></td>
               </tr>
               <tr>
                    <td>URL: </td>
                    <td><input name="${childId}_url" type="text" value="${url}">
                    </td>
               </tr>
            </table>
            <p>Позиции <img src="img/add.png" onclick="addEmptyPosition('${childId}')"/></p>
            <ul id="${childId}">
                <input name="${childId}_size" type="hidden" value="0">           
            </ul>
            <button type="button" onclick="removeLastChild('${childId}')">Удалить последнюю позицию  <img src="img/delete.png"></button>
        </li>`);
}

function addEmptyPosition(parentId) {
    addPosition(parentId, "1812-06", "1812-12", false, "");
}

function addPosition(parentId, sDate, eDate, isCurrent, title, description) {
    const list = document.getElementById(parentId);
    const counter = list.getElementsByTagName("input")[0];
    const index = parseInt(counter.value);
    const childId = parentId + "_" + index;
    counter.value = (index + 1).toString();
    list.insertAdjacentHTML("beforeend", `
        <li>
            <table>
                <tr>
                    <td>С: </td>  <td><input type="month" name="${childId}_sDate" value="${sDate}"></td>
                    <td>По: </td> <td><input type="month" name="${childId}_eDate" id="${childId}_eDate" value="${eDate}"></td>
                    <td><input type="checkbox" name="${childId}_isNow" id="${childId}_isNow" value="${isCurrent}" 
                        onchange="disableBasedOnState('${childId}_eDate','${childId}_isNow')"></td><td>Текущая позиция</td>
                </tr>
                <tr><td>Название: </td><td colspan="5"><input size="80" type="text" name="${childId}_title" value="${title}"></td></tr>
                <tr><td>Описание: </td><td colspan="5"><textarea cols=80 rows=5 name="${childId}_descr"></textarea></td></tr>
            </table>
        </li>`);
}

function removeLastChild(parentId) {
    const list = document.getElementById(parentId);
    const counter = list.getElementsByTagName("input")[0];
    const index = parseInt(counter.value);
    if (index > 0) {
        const items = list.getElementsByTagName("li");
        list.removeChild(items[items.length - 1]);
        counter.value = (index - 1).toString();
    }
    return false;
}

function disableBasedOnState(targetId, controllerId) {
    document.getElementById(targetId).disabled = document.getElementById(controllerId).checked;
}