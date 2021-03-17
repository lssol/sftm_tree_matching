package mantu.lab.utils

public fun <T, G> HashMap<T, HashSet<G>>.pushAt(key: T, value: G): HashMap<T, HashSet<G>> {
    (this[key] ?: hashSetOf()).add(value)

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