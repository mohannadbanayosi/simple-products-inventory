$ ->
  $.get "/products", (products) ->
    $.each products, (index, products) ->
      name = $("<div>").addClass("name").text product.name
      brand = $("<div>").addClass("brand").text product.brand
      price = $("<div>").addClass("price").text product.price
      $("#products").append $("<li>").append(name).append(brand).append(price)