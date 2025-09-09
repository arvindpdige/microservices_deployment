db = db.getSiblingDB("e-commerce");

db.createCollection("init"); // This forces Mongo to create the DB
db.init.insertOne({ status: "initialized" });



db.createCollection("products");  // forces Mongo to create the database
db.products.insertOne({
  name: "Test Product",
  price: 9.99,
  category: "Sample"
});