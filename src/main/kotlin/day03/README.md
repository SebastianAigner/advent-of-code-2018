# Day 3
(from https://adventofcode.com/2018/day/3)

## Part 1 (abridged)
`A claim like #123 @ 3,2: 5x4 means that claim ID 123 specifies a rectangle 3 inches from the left edge, 2 inches from the top edge, 5 inches wide, and 4 inches tall. How many square inches of fabric are within two or more claims?`

### Approach
The input is a little bit more complicated, which is a great excuse to use [Regex destructuring](https://medium.com/@garnop/safe-concise-text-parsing-with-regex-destructuring-in-kotlin-b8f77ef1e30c) to parse it into Kotlin objects.

For each rectangle, we simply keep track of how many times it has been claimed on a sheet. I did this via a map. Finding the parts that are claimed multiple times is as easy as counting the number of map entries that have more than one claim.

Instead of manually taking, modifying and reassigning values in the map, we can make use of the [Compute](https://docs.oracle.com/javase/8/docs/api/java/util/Map.html#compute-K-java.util.function.BiFunction-) functionality provided to us by Java while still profiting from simple Kotlin syntax.

## Part 2 (abridged)
`What is the ID of the only claim that doesn't overlap?`

### Approach
A claim has no overlap if every square of it has _exactly one_ claim on it. We can end a check prematurely if we realize that there is more than one claim on any of the squares outlined by the claim.