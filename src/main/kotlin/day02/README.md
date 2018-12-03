# Day 2
(from https://adventofcode.com/2018/day/2)

## Part 1 (abridged)
`To make sure you didn't miss any, you scan the likely candidate boxes again, counting the number that have an ID containing exactly two of any letter and then separately counting those with exactly three of any letter. You can multiply those two counts together to get a rudimentary checksum and compare it to what your device predicts.`

### Approach
We want to figure out which of the IDs in the input have letters that appear _exactly two_ times and _exactly three times_, in order to multiply them and get our checksum.

To do this, we can make use of the `groupingBy` functionality in Kotlin's standard library ([find out more here](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.collections/grouping-by.html)). Note that this is different to the `groupBy` functionality!

## Part 2 (abridged)
`The boxes will have IDs which differ by exactly one character at the same position in both strings. What letters are common between the two correct box IDs?`

### Approach
The amount of letters that are different in two strings of the same length is called the [Hamming Distance](https://en.wikipedia.org/wiki/Hamming_distance). Using `zip`, we can quickly provide an implementation for checking the distance of two given IDs, and filter for the _only two_ IDs that have Hamming distance of `1` (an invariant implied by the puzzle).

As a bonus, we can also automatically generate the common substring, simply by once again zipping and filtering our two candidate strings.