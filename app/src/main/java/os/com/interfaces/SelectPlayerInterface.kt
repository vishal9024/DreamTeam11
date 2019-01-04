package os.com.interfaces

interface SelectPlayerInterface {
    fun onClickItem(tag: String, position: Int)
    fun onClickItem(tag: Int, position: Int, isSelected: Boolean)
}
