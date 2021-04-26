/*
 * Tree Matching library based on the SFTM algorithm.
 *
 * Copyright (C) 2021  Mantu, Sacha Brisset.
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see https://www.gnu.org/licenses/.
 */

package mantu.lab.utils

public fun <T, G> HashMap<T, HashSet<G>>.pushAt(key: T, value: G): HashMap<T, HashSet<G>> {
    if (this[key] != null)
        this[key]!!.add(value)
    else
        this[key] = hashSetOf(value)

    return this
}

public fun <T> List<T>.average(f: (v:T) -> Number): Double {
    var sum = 0.0
    var count = 0

    this.forEach {
        count++
        sum += f(it).toDouble()
    }

    return sum / count
}

public fun <U, T, G> Iterable<U>.toMap(projKey: ((v:U) -> T), projValue: ((v:U) -> G)): HashMap<T, G> {
    val kvPair = this.map { Pair(projKey(it), projValue(it)) }
    return HashMap(kvPair.toMap())
}

//public fun <T> List<T>.minBy(f: (v:T) -> Number): Double {
//    var min = 0.0
//
//    this.forEach {
//        val v = f(it).toDouble()
//        if(v < min)
//            min = v
//    }
//
//    return min
//}
