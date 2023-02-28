package machines

import regex._
import dfa._
import scala.Conversion

given Conversion[Char, RegularLanguage] = Character(_)

given Conversion[String, RegularLanguage] = str =>
    str.tail.foldLeft(str.head: RegularLanguage)(Concat(_, _))

extension (lang: RegularLanguage)
    def ||(other: RegularLanguage) = Union(lang, other)
    def ~(other: RegularLanguage) = Concat(lang, other)
    def <*> = Star(lang)
    def <+> = Concat(lang, Star(lang))
    def apply(n: Int): RegularLanguage = 
        if n <= 0 then Epsilon  // only reached if n starts < 1
        else if n == 1 then lang
        else Concat(lang, lang{n-1})
    // BONUS: range repetition -- used like {2,5}, but I have to use (2,5)
    // since there are multiple arguments
    def apply(lower: Int, upper: Int): RegularLanguage =
        if lower > upper then Epsilon  // only reached of lower starts > upper
        else if lower == upper then lang{lower}
        else Union(lang{lower}, lang(lower+1, upper))

    def toDFA(using alphabet: Set[Char]) = regexToDFA(lang, alphabet)

given Conversion[RegularLanguage, DFA] = lang =>
    lang.toDFA(using chars(lang))

def chars(lang: RegularLanguage): Set[Char] =
    lang match
        case Empty => Set()
        case Epsilon => Set()
        case Character(c) => Set(c)
        case Union(lang1, lang2) => chars(lang1) ++ chars(lang2)
        case Concat(lang1, lang2) => chars(lang1) ++ chars(lang2)
        case Star(lang1) => chars(lang1)
    