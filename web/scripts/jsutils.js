function addOrganization(parentId) {
    var ol = document.getElementById(parentId);
    var counter = ol.getElementsByTagName("input")[0];
    var index = parseInt(counter.value);
    var childId = parentId + "_" + index;
    counter.value = index + 1;

    var name = document.createElement("input");
    name.type = "text";
    name.name = childId + "_name";
    var url = document.createElement("input");
    url.type = "text";
    url.name = childId + "_url";
    url.id = url.name;
    var add = document.createElement("img");
    add.src = "img/add.png";
    add.onclick = function () {
        addPosition(childId)
    };
    var list = document.createElement("ol");
    list.id = childId;
    var pos_counter = document.createElement("input");
    pos_counter.type = "hidden";
    pos_counter.name = childId + "_size";
    pos_counter.value = 0;
    var li = document.createElement("li");

    li.appendChild(document.createTextNode("Наименование: "));
    li.appendChild(name);
    li.appendChild(document.createTextNode("URL: "));
    li.appendChild(url);
    var h = document.createElement("h4");
    h.appendChild(document.createTextNode("Позиции: "));
    h.appendChild(add);
    li.appendChild(h);
    li.appendChild(list);
    list.appendChild(pos_counter);

    ol.appendChild(li);
}

function addPosition(parentId) {
    var ol = document.getElementById(parentId);
    var counter = ol.getElementsByTagName("input")[0];
    var index = parseInt(counter.value);
    var childId = parentId + "_" + index;
    counter.value = index + 1;
    var sd = document.createElement("input");
    sd.type = "month";
    sd.name = childId + "_sDate";
    var ed = document.createElement("input");
    ed.type = "month";
    ed.name = childId + "_eDate";
    ed.id = ed.name;
    var chk = document.createElement("input");
    chk.type = "checkbox";
    chk.name = childId + "_isNow";
    chk.id = chk.name;
    chk.checked = false;
    chk.onclick = function () {
        disableBasedOnState(ed.id, chk.id)
    };
    var desc = document.createElement("input");
    desc.type = "text";
    desc.name = childId + "_descr";
    desc.size = 80;
    var sDateText = document.createTextNode("С: ");
    var eDateText = document.createTextNode("По: ");
    var currentText = document.createTextNode("Текущая<br>Описание:");

    var li = document.createElement("li");
    li.appendChild(sDateText);
    li.appendChild(sd);
    li.appendChild(eDateText);
    li.appendChild(ed);
    li.appendChild(chk);
    li.insertAdjacentHTML("beforeend", "Текущая<br>Описание:<br>");
    li.appendChild(desc);
    ol.appendChild(li);
}

function disableBasedOnState(targetId, controllerId) {
    document.getElementById(targetId).disabled = document.getElementById(controllerId).checked;
}