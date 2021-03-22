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
