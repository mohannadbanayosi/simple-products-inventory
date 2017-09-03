This is a simple product inventory that you can add, list and search for products.

There are two ways to run the project:
1. ```sbt run```
2. Using docker:
    1. Build the image: ```sbt docker:publishLocal```
    2. Run: ```docker run -d --name inventory -p 9000:9000 simple-products-inventory:1.0-SNAPSHOT```

To add a product there are two ways:
1. Webpage: Got to http://localhost:9000/ and you will find a form.
2. Through the apis: A POST request to the endpoint /products/add with a body like this:
```
{
    "name": "air max",
    "brand": "nike",
    "imageUrl": "https://media.office.co.uk/medias/sys_master/root/h78/hf0/8967761428510.jpg?version=2.10",
    "price": 200
}
```

To list and search for the products, send a GET request to /products/browse with the following options:

![Alt text](/img/search_params.png?raw=true "Search params")
