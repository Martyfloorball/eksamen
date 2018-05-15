$(document).ready(function () {
  $.ajax({
    type: "POST",
    url: "/getProfile",
    success: function (data) {
      $("#fornavn").val(data[0][0]);
      $("#efternavn").val(data[0][1]);
      $("#email").val(data[0][2]);
      $("#telefon").val(data[0][3]);
      $("#stilling").val(data[0][4]);
      $.each(data[1], function (key, value) {
        $('#lokation').append('<input class="col-12 location" type="text" disabled value="'+ value +'"></input>')
      });
    }
  });
});