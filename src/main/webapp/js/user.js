function queryCallback(json) {
    console.log(json.count);
    layui.laypage.render({
        elem: 'page'
        , count: json.count
        , curr: json.page + 1
        , jump: function (obj) {
            console.log("json page:" + json.page + " curr:" + obj.curr)
            if (json.page != obj.curr - 1) {//为了防止查询回调时循环调用
                refresh(obj.curr - 1);
            }
        }
    });

    $("#tbody").empty();
    $.each(json.data, function (index, item) {
        console.log(item);
        var row = "<tr onclick='userQuery(" + item.id + ")'>"
            + "<td style='text-align: center'>" + item.id + "</td>"
            + "<td style='text-align: center'>" + item.username + "</td>"
            + "<td style='text-align: center'>" + item.phone + "</td>"
            + "</tr>";
        $("#tbody").append(row);
    })
}

function userQuery(id) {
    window.location.href = "/carexpert/user/" + id;
}

function refresh(page) {
    var url = "/carexpert/user/page/" + page;
    console.log('refresh' + page)
    $.get(url, queryCallback);
}

$("#form").submit(function () {
    $.post("/carexpert/user/save", $("#form").serialize(), function (data) {
        console.log(data)
        if (data.state == 200) {
            alert("添加成功")
            window.location.reload();
        } else {
            alert("添加失败")
        }
    })
    return false;
})

$(document).ready(function () {
    refresh(0)
})