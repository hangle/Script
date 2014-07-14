c
d 5/3/'size' argument changes the dimension
d of the input field and output text
d 10/--------script-----------
d c
d d \(#/size  12/ $xyz)
d x
d \(% /size 24/$xyz)
d -------------------------------
d
d 5/(#/size 12/ $xyz)
x
d
d 10/(% /size 24/ $xyz)
d 5/
d Different sizes

c
d 5/3/Test of the Parenthesized 'd' command
d such as:
d 10/d \(# $abc) // capture input
d d \(% $abc)    // display captured input
d d \(%% now is the time) //display text
d d \(y/n $yesNo) // either 'y' or 'n' input
d d \(#3 $choice) // choices 1,2 3
c
d 5/3/The following script test the \(# $abc)
d and the \(% $abc) parenthesized 
d expressions.
d 10/--------script-----------
d c
d d \(# $abc)
d x
d \(% $abc)
d -------------------------------
d 5/(# $abc)
x
d 10/(% $abc)
d 5/
d Your input should be displayed.
c
d The parenthesized expressions have
d appearance arguments, i.e., color.
d 10/--------script-----------
d c
d d \(#/color blue/ $xyz)
d x
d \(% /color green/$xyz)
d -------------------------------
d 5/(#/color blue/ $xyz)
x
d 10/(% /color green/ $xyz)
d 5/
d Your input should be displayed.
c
d 5/3/'name' argument changes the font name. 
d 10/--------script-----------
d c
d d \(#/name Ubuntu/ $xyz)
d x
d \(% /name Monospaced/$xyz)
d -------------------------------
d
d 5/(#/name Ubuntu/ $xyz)
x
d
d 10/(% /name Monospaced/ $xyz)
d 5/
d Input/output test with different
d fonts.

* end
