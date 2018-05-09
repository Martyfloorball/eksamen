$(document).ready(function () {

    $("#openCreateNewEmployee").click(function () {
        $("#overlay, #createNewEmployee").show();
    });

    $("#cancelEmployeeCreate, #overlay").click(function () {
        $("#overlay, .popup-formular").hide();
    });

  $.ajax({
    type: "POST",
    url: "/getEmployees",
    success: function (data) {


      $.each(data[0], function (key, value) {
        $('#table').append('<th>' + value + '</th>')
      });

      for (var i = 1; i < data.length; i++) {
        var string = '<tr>';
        $.each(data[i], function (key, value) {
          string += '<td>' + value + '</td>'
        });
        string += '</tr>';
        $('#table').append(string)
      }
      console.log(data);
    }
  })
});