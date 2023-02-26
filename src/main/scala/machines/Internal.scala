package machines

import regex._
import dfa._
import scala.Conversion

given Conversion[Char, RegularLanguage] = Character(_)

given Conversion[String, RegularLanguage] = from =>
    from.tail.foldLeft(from.head: RegularLanguage)(Concat(_, _))

extension (lang: RegularLanguage)
    def ||(other: RegularLanguage) = Union(lang, other)
    def ~(other: RegularLanguage) = Concat(lang, other)
    def <*> = Star(lang)
    def <+> = Concat(lang, Star(lang))
    def apply(n: Int) = {
        var rep: RegularLanguage = Epsilon
        for (i <- 1 to n) rep = Concat(lang, rep)
        simplify(rep)
    }