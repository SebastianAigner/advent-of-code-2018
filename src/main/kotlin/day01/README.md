# Day 1
(from https://adventofcode.com/2018/day/1)

## Part 1 (abridged)
`Starting with a frequency of zero, what is the resulting frequency after all of the changes in frequency have been applied?`

### Approach
The resulting frequency is simply the sum of the individual deltas / changes.

## Part 2 (abridged)
`What is the first frequency your device reaches twice?`

### Approach
The simplest approach is just to loop over the collection of elements while adding them to a set and checking whether we'd be adding a duplicate.

I have decided to use this opportunity and learn about implementing a `CircularList` including iterator in Kotlin!