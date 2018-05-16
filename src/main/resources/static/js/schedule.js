$(document).ready(function () {
    $(".openCreateNewShift").click(function () {
        $("#overlay, .createNewShift").show();
    });
    $(".cancelCreateNewShift, #overlay").click(function () {
        $("#overlay, .popup-formular").hide();
    });

    /*$("input[name=filterLocations]").click(function () {
        getList();
    });*/
});

/*function getList() {
    var filterLocations = [];
    $('input[type=checkbox]').each(function () {
        if ($(this).prop("checked") == true) {
            filterLocations.push($(this).val());
        }
    });
}*/