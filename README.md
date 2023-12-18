# product-catalog-service
Microservice for managing product catalog


## How to Access
### This app contains 4 Controllers
- ProductController (Rest Controller)
    - Create Product
        > POST :: products/addProduct
        > localhost:9092/products/addProduct

    - Get all Products
      - Returns List of GenericProductDto
      > GET
      > localhost:9092/products/getAllProducts

  - Get Product By Id
      - Returns a GenericProductDto
      > GET
      > localhost:9092/products/getProductById/7c7303b8-1fd5-4170-9d1c-4756e91ccc2e