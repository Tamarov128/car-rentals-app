# Assignment 8

Generate at least 100.000 entities (patiens, birthday cakes, cars) and use multithreading to perform a bulk update operation on the entities, e.g.:

- Patients: add a health risk status attribute (_high risk_, _lower risk_) and update the health risk status of all patients over 60 years old to _high risk_.
- Birthday Cakes: update the price of all vanilla cakes to be 20% more.
- Cars: update the price of all cars newer than 10 years to be 20% more.

Use a configurable number of threads. Each thread is responsible with updating a certain subset of the data. Use both traditional Java Threads and the **ExecutorService**.



**Bonus (0.1p)**

Implement a multi-threaded sorting algorithm to sort the collection of entities (patiens, birthday cakes, cars).
