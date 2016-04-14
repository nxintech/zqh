def insertionSort(array) {
    for (int i = 1; i < array.size(); i++) {
        def X = array[i]
        def index = i - 1
        while (index >= 0 && array[index] > X) {
            array[index + 1] = array[index]
            index--
        }
        array[index + 1] = X
    }
    array
}

println insertionSort([7, 0, 3, 6, 4])

def ShellSort(array) {
    def length = array.size()
    def h = length.intdiv(2)
    while (h > 0) {
        for (i = h; i < length; i++) {
            for (j = i; j > 0; j = j - h) {
                if (array[j - h] > array[j]) {
                    swap(array, j - h, j)
                } else {
                    break
                }
            }
        }
        h = h.intdiv(2)
    }
    return array
}

println(ShellSort([4, 6, 2, 8, 0, 7, -1]))
