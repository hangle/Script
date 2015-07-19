c
d 5/3/The (%%/color blue/Add) command whose tag is (%%/color blue/+) 
d performs the same function as the Clear  
d 'c' command.
d
d It is the initial command of a special
d CardSet that is termed the (%%/color blue/AddCardSet).
* continue
d
d The command members of the AddCardSet
d are the same members as those of the 
d CardSet.

c
d 5/3/An example of the AddCardSet:
d
d 10/------------script-----------------
d +
d d Display text of an
d d AddCardSet
d -------------------------------------
* continue
d 5/
d However, the AddCardSet is only 
d associated with the immediately 
d preceeding CardSet.
d
d
d 10/This association is yet to
d be disclosed.

c
d 5/3/An example of a  CardSet and its
d associated AddCardSet.
d
d 15/--------------script---------------
d c
d d Display text of a 
d d CardSet
d
d +
d d Display text of an
d d AddCardSet
d -------------------------------------
d 5/
* continue
d More than one AddCardSet can be
d associated with a particular CardSet.

c 
d 5/2/
d 15/--------------script---------------
d c
d d Display text of a 
d d CardSet
d
d +
d d Display text of an
d d AddCardSet
d -------------------------------------
d 5/
d The CardSet enables the (%%/color blue/+Add) button
d (color change from gray to yellow).
d
d This change only occurs if the CardSet has
d one or more, associated AddCardSets.

c
d 5/2/
d 15/--------------script---------------
d c
d d Display text of a 
d d CardSet
d
d +
d d Display text of an
d d AddCardSet
d -------------------------------------
d 5/
d
d While the CardSet is active and the +Add
d button is enabled, the user had the following
d choice.
d
d Activate the +Add button to execute the
d AddCardSet.
d
d Activate the Next button to skip the
d AddCardSet.

c
d 5/1/
d 15/--------------script---------------
d c
d d Display text of a 
d d CardSet
d
d +
d d Display text of an
d d AddCardSet
d -------------------------------------
d 5/
d In the event the +Add button is activated
d and the AddCardSet executes. 
* continue
d
d From the AddCardSet, the +Add button
d activation (%%/color blue/returns) the user to the 
d associated CardSet.

c
d 5/2/A practical example of the AddCardSet is
d a learning situation.  A user, unsure of an
d answer, may benefit from a hint or probe.
d
d 15/----------script----------------
d c
d Ohio's capital is \(# $columbus)
d +
d d Discover of the New World
d +
d d Eight letters beginning with 'C'
d c ...
d ----------------------------------
d 5/
* continue
d Rather than guess the answer, the user
d hits the +Add button.
d
d The user returns to answer the
d question.

c
d 5/3/The user who knows that the capital
d of Ohio is Columbus will answer the
d question and skip the two AddCardSets.
* continue
d
d In a series of two or more AddCardSets 
d associated with a CardSet, the +Add 
d button executes this series and 
d eventually returns to the CardSet.
d
d In the series, activation of the Next
d button initiates the return from any
d AddCardSet. 

c
d 5/2/The next CarsSet executes the following
d script
d 15/----------script----------------
d c
d Ohio's capital is \(# $columbus)
d +
d d Discover of the New World
d +
d d Eight letters beginning with 'C'
d c ...
d ----------------------------------
d 5/
d To begin, activate the +Add button.

c
d Ohio's capital is (# $columbus)
d 20/17/size 10/style bold/hit +Add button
+
d Discover of the New World
d 20/17/size 10/style bold/hit +Add button
+
d Eight letters beginning with 'C'
d 20/17/size 10/style bold/hit +Add button

c
d 5/3/In a series of AddCardSets associated
d with a particular CardSet, the series
d has its own backup mechanism (Prior key).
d
d The Prior key is disabled for the first
d AddCardSet since there is no prior
d AddCardSet.

c
d 5/3/This completes the survey of the
d AddCardSet '+' command.

f end
* end
