import requests
import argparse

zalando_url = "https://api.zalando.com/articles/?page={}&pageSize={}"
products_inventory_url = "http://localhost:9000/products/add"


def main():
    parser = argparse.ArgumentParser(description="Gets products from zalando and adds it to the inventory.")

    parser.add_argument('batch_size', metavar="Batch size", type=int,
                        help="Number of products to get from zalando api per request. 1 to 200")

    parser.add_argument('batches_number', metavar="Number of batches", type=int,
                        help="Number of requests sent to zalando api. Min: 1")

    args = parser.parse_args()
    batch_size = args.batch_size
    batches_number = args.batches_number

    for i in range(1, (batches_number+1)):
        print ("connecting to zalando api to retrieve batch number {}/{} of size {}".format(i, batches_number,
                                                                                            batch_size))
        zalando_data = requests.get(zalando_url.format(i, batch_size))
        products = zalando_data.json()["content"]

        new_product = {}

        for product in products:
            new_product["name"] = product.get("name", "NA")
            new_product["brand"] = product["brand"].get("name", "NA")
            new_product["imageUrl"] = product["media"]["images"][0].get("mediumHdUrl", "NA")
            new_product["price"] = int(product["units"][0]["price"].get("value", "NA"))

            requests.post(products_inventory_url, new_product)

        print ("added {} products to the inventory".format(batch_size))

    print ("a total of {} prducts added to the inventory".format(batch_size*batches_number))


if __name__ == "__main__":
    main()
