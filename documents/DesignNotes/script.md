<h1>Script Program</h1>

<p>The Script program validates the syntax of commands in a file <br />
whose extension is '<em>.nc', such as, 'nowis.nc'.  The program's <br />
output is a file whose extension  is '</em>.struct', such as, <br />
'nowis.struct'.  The 'nowis.struct' file is executed by the <br />
Notecard program to create a series of card-size windows.     </p>

<p>The following are the script commands of the 'nowis.nc' <br />
file. The letters beginning  the commands (c,d,*) are <br />
command tags.</p>

<pre>
        c
        d now is
        c
        d the time
        * end
</pre>

<p>The Script output from 'nowis.nc' is the 'nowis.struct' <br />
file.  </p>

<pre>
                 %Notecard                        %CardSet
                 child       2002                  child    2006
                 height 300                        address  2005
                 width  400                        siblinng 2008
                 font_size  14                     name        0
                 asteriskButton on                 condition   0
                 priorButton    on                 %%
                 %%

                 %CardSet                         %RowerNode
                 child      2003                  child     2007
                 address    2002                  address   2006
                 sibling    2005                  sibling   0
                 name       0                     row       0
                 condition  0                     column    0
                 %%                               %%

                 %RowerNode                       %DisplayText
                 child      2004                  address   2007
                 address    2003                  sibling   0
                 sibling    0                     style   14
                 row    0                         column  0
                 column 0                         name TimesRoman
                 %%                               text the time
                                                  color black
                 %DisplayText                     %%
                 address    2004
                 sibling    0                     %NotecardTask
                 style  1                         address   2008
                 size   14                        sibling   0
                 column 0                         task   end
                 name   TimesRoman                type   0
                 text   now is                    %%
                 color  black
                 %%
</pre>

<p>No explanation of the 'nowis.struct' files is now offered. <br />
Explanation   begins   with the  subsystem ParserEvaluator <br />
and its input file, i.e.,  'nowis.nc'.    </p>

<p>The Script program has two major tasks. The first is to validate <br />
the syntax   and values of the script commands. The second is to <br />
build the '*.struct' file. The execution of Script's 'main' <br />
function  invokes  two major subsystems:  </p>

<pre><code>    ParserValidator(..)  
    BuildStructure(..)
</code></pre>

<h2>ParserValidator Subsystem  </h2>

<p>The Notecard program is almost void of error checking syntax. <br />
It is the   responsibility of the Script program to catch <br />
and report potential errors. A syntax error or an invalid <br />
script value throws an exception, causing the defective <br />
'*.nc' file line to be printed along with a brief message <br />
explaining the error. </p>

<p>The Script program begins with validating the script <br />
commands.  The script   command tags of '.nowis.nc' (c, d, *) <br />
selects the following error checking functions :  </p>

<pre><code>    scriptCommandTag match {    
        case 'c' =&gt;   CardCommand . cardCommand(...)  
        case 'd' =&gt;   DisplayCommand . displayCommand(...)  
        case '*' =&gt;   AsteriskCommand . asteriskCommand(...)  
        }
</code></pre>

<p>These three classes create  output in the 'nowis.struct' <br />
file that has a common structure:  </p>

<pre><code>    %&lt;classname&gt;  
    key1    argument 1  
    .  
    .  
    .  
    keyN    argument N  
    %%
</code></pre>

<p>CardCommand's output for the first 'c' script  is:  </p>

<pre><code>    %CardSet  
    name    0
    condition 0  
    %%
</code></pre>

<p>Had the Card command 'c' been:  </p>

<pre><code>    c  ($abc)=(John)
</code></pre>

<p>Then the output would have been:  </p>

<pre><code>    %CardSet  
    name    0 
    condition   ($abc)=(John)  
    %%
</code></pre>

<p>The AsteriskCommand's output for the '* end' script is:  </p>

<pre><code>    %NotecardTask  
    task    end  
    type      0
    %%
</code></pre>

<p>DisplayCommand  output for the first 'd' script is:  </p>

<pre><code>    %DisplayText  
    style   0  
    size    16  
    column 0  
    name    TimesRoman  
    text    now is  
    color   black   
    %%
</code></pre>

<p>The Display 'd' command is  complex with a number of features. <br />
For each 'd' command,  the DisplayCommand object creates:  </p>

<pre><code>    %RowerNode  
    column    &lt;value&gt;  
    row         &lt;value&gt;  
    %%
</code></pre>

<p>A  'd' command   occupies a particular  window row and <br />
begins in a specific   column.  When the 'd' command does <br />
not specify a row or starting column, the values are 0. <br />
The following command does so:  </p>

<pre><code>    d 5/13/now is  

    %RowerNode  
    column  5  
    row 13  
    %%
</code></pre>

<p>The '%Notecard...%%' group in the 'nowis.struct' begins <br />
each '*.nc' file.  The key/values (height 300, width 400) <br />
determines the size of the Notecard window and the key/value <br />
(font_size) establishes the letter size.  </p>

<pre><code>    %Notecard  
    height      300  
    width      400  
    font_size 14  
    asteriskButton  on
    priorButton     on
    %%
</code></pre>

<p>The other command tags that the ParserValidator subsystem uses <br />
in the match  statement are  ('a', 'e', 'g', 'f', and 'x')  :</p>

<pre><code>    case 'a'  =&gt;  Assigner . assignerCommand(...)  
    case 'e'  =&gt;  EditCommand  .  editCommand(...)  
    case 'g'  =&gt;  GroupCommand  .   groupCommand(...)  
    case 'f'   =&gt;  NextFile  .  nextFile(...)  
    case 'x'  =&gt;  Xecute  .  xecuteCommand(...)
</code></pre>

<p>The other important role that  the ParserValidator subsystem <br />
performs is to evaluate each command and to raise an exception <br />
when the command syntax is invalid.   SyntaxException class <br />
displays the line containing the invalid syntax and a <br />
description of the error. The 'nowis.nc' file is shown <br />
with a syntax   error.  The 'd' command lacks a space <br />
following the 'd' tag:  </p>

<pre><code>    c  
    dnow is  
    c  
    d for all  
    * end
</code></pre>

<p>The  following  error message is printed:  </p>

<pre><code>    :line=  
        dnowis  
    error:   space NOT following symbol
</code></pre>

<p>The   '*  end' command is misspelled:  </p>

<pre><code>    c   
    d now is   
    c   
    d for all   
    * emd
</code></pre>

<p>The following is printed:  </p>

<pre><code>    :line=  
        * emd  
    error:  unknown key: emd
</code></pre>

<p>The syntax checking of this subsystem is extensive and its <br />
specifices are  not covered here.  </p>

<h2>BuildStructure  Subsystem </h2>

<p>The completion of 'ParserValidator' delivers the following <br />
List[List[String]] to 'BuildStructure' (each <br />
'%<className>...%%' is a list element of List).    </p>

<pre>
                %Notecard                       %RowerNode
                height  300                     row      0
                width   400                     column   0
                font_size   14                  %%
                asteriskButton  on
                priorButton on                  %DisplayText
                %%                              style  1
                                                size   14 
                %CardSet                        column 0
                name        0                   name   TimesRoman
                condition       0               text   the time
                %%                              color  black
                %RowerNode                      %%
                row 0
                column  0                       %NotecardTask
                %%                              task   end
                                                type   0
                %DisplayText                    %%   
                style   1                       
                size    14
                column  0
                name    TimesRoman
                text    now is
                color   black
                %%

                %CardSet
                name        0
                condition       0
                %%
</pre>

<p>The above List resembles the elements of the 'nowis.struct' <br />
file presented  earlier.  On closer inspections of the two, the <br />
'child', 'address', and 'sibling' elements  of the file are <br />
missing from the above List.   </p>

<p>The following shows a scaled down version of 'nowis.struct' <br />
with only the   %<className>  and the 'child', address, and <br />
'sibling' elements  </p>

<pre>
        %Notecard
        child  2002
                        %CardSet
                        child  2003
                        address 2002
                        sibling 2005
                                        %RowerNode
                                        child   2004
                                        address 2003
                                        sibling 0
                                                        %DisplayText
                        %CardSet                        address  2004
                        child   2006
                        address 2005
                        sibling 2008
                                        %RowerNode
                                        child   2007
                                        address 2006
                                        sibling 0
                                                        %DisplayText
                        %NotecardTask                   address  2007
                        address 2008
                        sibling 0
</pre>

<p>The scaled down version of 'nowis.struct' reveals the linked <br />
list structure.  A parent class element 'child' references <br />
the 'address' of its first child  whose 'sibling' elements <br />
references other parent children.  For example, The parent <br />
'Notecard' is linked to its   three children ('CardSet's and <br />
'NotecardTask').  </p>

<p>The initial BuildStructure task is to create class instances <br />
with %<class names>.  The 'nowis.struct' file has the following <br />
class names:  </p>

<pre>
        %Notecard  
        %CardSet  
        %RowerNode  
        %DisplayText  
        %CardSet  
        %RowerNode  
        %DisplayText  
        %NotecardTask  
</pre>

<p>BuildStructure invokes CommandLoader.createObject to instantiate <br />
objects.  </p>

<pre><code>    “ %&lt;classname&gt; “ match {  
        case %Notecard  =&gt;   NotecardCmd( &lt;class arguments&gt;)  
        case %CardSet   =&gt;   CardSetCmd( &lt;class arguments)  
        case %RowerNode=&gt;    RowerNodeCmd( &lt;class arguments&gt;)  
        case %DisplayText=&gt;  DisplayTextCmd( &lt;class arguments&gt;)  
        case %NotecardTask=&gt; NotecardCmd( &lt;class arguments&gt; )
</code></pre>

<p>The classes,like NotecardCmd, are case classes.  The class <br />
instances are passed arguments. NotecardCmd, for example, <br />
is passed the following arguments:  </p>

<pre>
            height  300
            width   400
            font_size   14
            asteriskButton  on
            priorButton on    
</pre>

<p>There are 13 case classes of the type '<classname>Cmd' <br />
representing script commands as well as components of these <br />
commands.  The above code is shown to just handles the <br />
simple  'nowis.nc' script.  </p>

<p>The '<classname>Cmd objects are assembled into a List of <br />
Any type.   The NotecardCmd object is at the head of this <br />
List.  Every List object is given a unique index or Id. <br />
Starting with NotecardCmd ,  it is assigned   2002 as an <br />
Id value, and each successive   List objects is assigned <br />
an incremented value.  Values 2002 to 2002 + n serve as <br />
symbolic addresses of the '<classname>Cmd objects.   </p>

<h2>Structure.</h2>

<p>The final step of the Script program is to organize the <br />
'<classname>Cmd' into a structure of linked lists where <br />
the root of this structure is  NotecardCmd. The Notecard <br />
'card' program general approach is to iterate a series of <br />
linked lists.  The documentation of 'notecard.md' in the <br />
Notecard repository details the specifics of this linked <br />
list iteration.  </p>

<p>The Notecard program's linked list structure has the <br />
same structure as that of the Script program structure <br />
shown below. A '<classname>Cmd' classes create the linked <br />
list structure.</p>

<pre>
    <clsssname>Cmd  Types                       Script Examples    

    NotecardCmd  
        LoadDictionaryCmd                l
            AssignCmd                    a $count=0
        NotecardTaskCmd                  * end    
        NextFileCmd                      f maleScript    
        CardSetCmd                       c (1)=(2)    
            AssignCmd                    a $count=$count+1    
            XNodeCmd                     x  
            GroupCmd                     g (1) = (1)  /
            CardSetTask                  * continue  
            RowerNodeCmd                 d 3/5/  
                DisplayTextCmd           d now is  
                DisplayVariableCmd       d (% $count)  
                BoxFieldCmd              d (# $name)  
                    EditCmd              e ($count) < (5)  
</pre>

<p>NotecardCmd has 4 children types (LoadDictionaryCmd, <br />
NotecardTaskCmd,   NextFileCmd, and CardSetCmd).  The <br />
sibling CardSet has 5 children types.  RowerNodeCmd and <br />
BoxField are siblings as well as parent types.   </p>

<p>Our List objects of <classname>Cmd type have been assigned <br />
symbolic addresses but each object lacks  the symbolic address <br />
of the object to which it is linked.   In the case of a parent <br />
type, the object lacks a link to its next sibling  object and <br />
to its first child object.  It is the role of the parent classes <br />
(NotecardCmd, CardSetCmd, RowerNodeCmd, and   BoxFieldCmd) to <br />
establish the linkage of symbolic addresses.   </p>

<p>Each parent class instantiates 'NodeParent' that has two fields <br />
('firstChild'  and 'tail').  'firstChild' references the initial <br />
child and 'tail' references the  last child  added to the list. <br />
The function that builds the linked list is Link.append(...) a <br />
trait of the parent class.  It is important to note that objects <br />
will be linked by their physical addresses.   </p>

<p>The means by which the physical addresses are linked is to take <br />
NotecardCmd off the top of the List and to pass all other <br />
<classname>Cmd  to NotecardCmd. NotecardCmd  “recognizes” it <br />
children by the following match statement:  </p>

<pre><code>        val parent = new NodeParent  
        var cardSet=null  
        . . .  
        c=&lt;next List element&gt;  
        c  match {  
            case ld: LoadDictionaryCmd=&gt;  Link.append(parent, ld)
            case nf: NextFileCmd=&gt;        Link.append(parent, nf)  
            case ft: NotecardTaskCmd=&gt;    Link.append(parent, ft)  
            case cs: CardSetCmd=&gt;         Link.append(parent, cs)  
                                  cardSet=cs  
            case _=&gt;cardSet.attach(c)
</code></pre>

<p>When a parent does not recognize an object as its child, then <br />
<next List element>   passes the object off  to another parent <br />
class type.  In the example, CardSet.attach(c) is invoked.  The <br />
other parent class have similar code.  In the case of an EditCmd <br />
object,  it  passes through three parent types before it is <br />
delivered to its parent,  i.e., BoxFieldCmd.   </p>

<p>The translation of physical addresses to symbolic addresses is <br />
handled by the method 'postIds' which is common to all <br />
<classname>Cmd classes.  The following example uses 'postIds' <br />
in CardSetCmd.  </p>

<pre><code>    def  postIds {  
        postChild  
        postNextSibling  
        }
</code></pre>

<p>CardSetCmd is a parent of other <classname>Cmd objects as well <br />
as a child of   NotecardCmd.  It must find two symbolic <br />
addresses.  It holds the reference to  its first child enabling <br />
it to return child's symbolic address.  It also holds  the <br />
reference to its next sibling to return the siblings symbolic <br />
address  .</p>

<p><classname>Cmd classes that are not parents need only reference <br />
the sibling that it is linked to, thus:  </p>

<pre><code>    def postIds {  
        postNextSibling  
        }
</code></pre>

<p>Finally with each <classname>Cmd object in List having a symbol <br />
address, the symbolic addresses of other objects, and the <br />
argument list passes to it when created, then the List is <br />
iterated to have  each object print this information to  the <br />
'.struct' file.   </p>
