function post() {
    var questionId = $("#question_id").val();
    var content = $("#comment_content").val();

    if (!content) {
        alert("不能回复空内容！")
        return;
    }

    $.ajax({
        type: "POST",
        url: "/comment",
        data: JSON.stringify({
            "parentId":questionId,
            "content":content,
            "type":1,
        }),
        contentType:'application/json',
        success: function (response) {
            if (response.code == 200) {
                window.location.reload();
            } else {
                if (response.code == 2003) {
                    var isAccepted = confirm(response.message);
                    if (isAccepted) {
                        // Window.localStorage.setItem("closable", true);
                        window.open("https://github.com/login/oauth/authorize?client_id=0722d115b46361e6bec5&redirect_uri=http://localhost:8887/callback&scope=user&state=1");
                        localStorage.setItem("closable","true");
                    }
                } else {
                    alert(response.message);
                }
            }
        },
        dataType: "json"
    });
}