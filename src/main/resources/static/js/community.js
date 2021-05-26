/**
 * 提交回复
 */
function post() {
    var questionId = $("#question_id").val();
    var content = $("#comment_content").val();

    comment2target(questionId, 1, content);
}

function comment2target(targetId, type, content) {
    if (!content) {
        alert("不能回复空内容！")
        return;
    }

    $.ajax({
        type: "POST",
        url: "/comment",
        data: JSON.stringify({
            "parentId": targetId,
            "content": content,
            "type": type,
        }),
        contentType: 'application/json',
        success: function (response) {
            if (response.code == 200) {
                window.location.reload();
            } else {
                if (response.code == 2003) {
                    var isAccepted = confirm(response.message);
                    if (isAccepted) {
                        // Window.localStorage.setItem("closable", true);
                        window.open("https://github.com/login/oauth/authorize?client_id=0722d115b46361e6bec5&redirect_uri=http://localhost:8887/callback&scope=user&state=1");
                        localStorage.setItem("closable", "true");
                    }
                } else {
                    alert(response.message);
                }
            }
        },
        dataType: "json"
    });
}

function comment(e) {
    var commentId = e.getAttribute("data-id");
    //content获取的是input输入的值
    var content = $("#input-" + commentId).val();
    comment2target(commentId, 2, content);
}


/*
展开二级评论
 */
function collapseComments(e) {
    var id = e.getAttribute("data-id");
    // 通过传入的问题id确定展开哪个评论
    var comments = $("#comment-" + id);
    //获取二级评论展开状态
    var collapse = e.getAttribute("data-collapse");
    if (collapse) {
        //折叠二级评论
        comments.removeClass("in");
        e.removeAttribute("data-collapse");
        e.classList.remove("active");
    } else {
        var subCommentContainer = $("#comment-" + id);

        if (subCommentContainer.children().length !=1) {
            //展开二级评论
            comments.addClass("in");
            //标记二级评论展开状态
            e.setAttribute("data-collapse", "in");
            e.classList.add("active");
        } else {
            $.getJSON("/comment/" + id, function (data) {
                $.each(data.data.reverse(), function (index, comment) {

                    var mediaLeftElement = $("<div/>", {
                        "class":"media-left",
                    }).append($("<div/>", {
                        "class": "media-object img-rounded",
                        "src": comment.user.avatarUrl,
                    }));

                    var mediaBodyElement = $("<div/>", {
                        "class":"media-body",
                    }).append($("<h5/>", {
                        "class": "media-heading",
                        "html": comment.user.name,
                    })).append($("<h5/>", {
                        "html": comment.content
                    })).append($("<h5/>", {
                        "class":"menu"
                    })).append($("<span/>", {
                        "class":"pull-right ",
                        "html":moment(comment.gmtCreate).format('YYYY-MM-D')
                    }));

                    var mediaElement = $("<div/>", {
                        "class": "media",
                    }).append(mediaLeftElement)
                        .append(mediaBodyElement);

                    var commentElement = $("<div/>", {
                        "class": "col-lg-12 col-md-12 col-sm-12 col-xs-12 comments",
                    }).append(mediaElement);

                    //循环插入
                    subCommentContainer.prepend(commentElement);
                });

                //展开二级评论
                comments.addClass("in");
                //标记二级评论展开状态
                e.setAttribute("data-collapse", "in");
                e.classList.add("active");
            });
        }


    }



}


function selectTag(value) {
    var value = value.getAttribute("data-tag")
    var previous = $("#tags").val();
    if (previous.indexOf(value) == -1) {
        if (previous) {
            $("#tags").val(previous+','+value);
        }else {
            $("#tags").val(value);
        }
    }

}

function showSelectTag() {
    $("#select-tag").show();
}
