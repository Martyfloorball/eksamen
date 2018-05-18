$(document).ready(function () {
    $("#repeat").click(function (e) {
        $(".weekDays, .date-end").toggle();
        console.log("hej");
        e.preventDefault();
    });
});
