# Reflection on implementing regular expressions of a DSL

## Which operators were easiest to implement and why?

It was very easy to implement the `||`, `~`, and `<*>` operators since they were
already basic classes of the `RegularLanguage` trait: `Union`, `Concat`, and
`Star` respectively.
So it was effectively just a renaming of these existing classes.
It was also pretty straightforward to implement `<+>` since it was just a
combination of two existing classes: `Concat` and `Star`.

## Which operators were most difficult to implement and why?

The `{n}` operator was definitely the most difficult to write because there were
a lot of different approaches that could be used.
I started by sing a for loop, but this required defining a variable before the
loop that is repeatedly modified, so I then tried using a recursive approach.
The recursive approach seemed much cleaner with the only catch being that I had
to check for `n == 1` if I wanted to avoid concatenating to `Epsilon` when
`n > 0`, which wasn't necessary technically, but does make the result cleaner.
I then tried using `Array.fill` with `reduce` and this eliminated the need for
the extra `n == 1` check, but the code was more difficult to read, so I opted to
go with the recursive method instead.

## Comment on the design of this internal DSL

Write a few brief paragraphs that discuss:

- What works about this design? (For example, what things seem easy and
  natural to say, using the DSL?)
- What doesn't work about this design? (For example, what things seem
  cumbersome to say?)
- Think of a syntactic change that might make the language better. How would
  you implement it _or_ what features of Scala would prevent you from
  implementing it? (You don't have to write code for this part. You could say
  "I would use extension to..." or "Scala's rules for valid
  identifiers prevent...")

It seems easy to create basic regular languages with any combinations of
characters, unions, concatenations, repetitions, etc.
Additionally, this design allows for easy simplification of the regular language
expressions, which may be more of a feature for the implementer than the user.
This language also makes it easy to see if a regular language matches a string
which seems to be the main purpose of this language.

This design does make it difficult to do more abstract pattern matching like
using wildcard characters or ranges of characters.
Compared to regular expression syntax, this also is a bit more cumbersome since
it requires spaces between operators, single quotes for characters, and double
quotes for strings.
The repetition characters also require angle brackets since the `*` and `+`
operators already have functions defined for characters.
This design also doesn't apply adjacent functionality like searching for
matches within strings.
Also, more broadly, it cannot be used for operations out of its domain like
doing basic mathematical calculations.

One syntactic improvement could be to make the `<*>` and `<+>` operators apply
only to the expression directly before them.
Right now, it seems the only way to achieve this is to put parentheses around
the expression and the `<*>` or `<+>` operators.
This syntax change seems challenging if not impossible due to the nature of how
the operators are evaluated from left to right, but Scala has operators that
have an order (like `*` and `/` before `+` and `-`), so maybe there's a way.

Another helpful syntactic addition would be to make the output of the regular
languages more readable.
This could be done by overriding the `toString` method for the `RegularLanguage`
trait.
Within this method, recursive pattern matching could be used to determine what
string to show. For example, `Empty` could return `∅`, `Charcater('c')` could
show `c`, `Concat('a', 'b')` could show `ab`, etc.
This wouldn't directly help the user type the language, but it would help them see what they type which could be helpful if they are doing operations on with
languages stored in variables rather than typing it all out.