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