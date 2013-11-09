<h1>Script Program</h1>

<p>The Script program validates the syntax of commands in a file whose extension
is '<em>.nc', such as, 'nowis.nc'.  The program's output is a file whose extension
is '</em>.struct', such as, 'nowis.struct'.  The 'nowis.struct' file is <br />
executed by the Notecard program to create a series of card-size windows.   </p>

<p>The following are the script commands of the 'nowis.nc' file. The letters beginning
the commands (c,d,*) are command tags.</p>

<pre>
                                c
                                d now is
                                c
                                d the time
                                * end
</pre>

<p>The Script output from 'nowis.nc' is the 'nowis.struct' file.</p>

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

<p>No explanation of the 'nowis.struct' files is now offered.  Explanation begins with 
the  subsystem ParserEvaluator and its input file, i.e., 'nowis.nc'.</p>

<p>The Script program has two major tasks. The first is to validate the syntax and values 
of the script commands. The second is to build the '*.struct' file. </p>

<p>The execution of Script's 'main' function  invokes  two major subsystems:  </p>

<pre><code>    ParserValidator(..)  
    BuildStructure(..)
</code></pre>

<h2>ParserValidator Subsystem  </h2>

<p>The Notecard program is almost void of error checking syntax. It is the responsibility of 
the Script program to catch and report potential errors.  A syntax error or an invalid script 
value throws an exception, causing the defective '*.nc' file line to be printed along with
a brief message explaining the error. </p>

<p>The Script program begins with validating the script commands.  The script command tags of 
'.nowis.nc' (c, d, *) selects the following error checking functions :  </p>

<pre><code>    scriptCommandTag match {    
        case 'c' =&gt;   CardCommand . cardCommand(...)  
        case 'd' =&gt;   DisplayCommand . displayCommand(...)  
        case '*' =&gt;   AsteriskCommand . asteriskCommand(...)  
        }
</code></pre>

<p>These three classes create  output in the 'nowis.struct' file that has a  common structure:  </p>

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

<p>Had the Clard command 'c' been:  </p>

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

<p>The Display 'd' command is  complex with a number of features.  For each 'd' command, <br />
the DisplayCommand object creates:  </p>

<pre><code>    %RowerNode  
    column    &lt;value&gt;  
    row         &lt;value&gt;  
    %%
</code></pre>

<p>A  'd' command   occupies a particular  window row and begins in a specific column. <br />
When the 'd' command does not specify a row or starting column, the values are 0. <br />
The following command does so:  </p>

<pre><code>    d 5/13/now is  

    %RowerNode  
    column  5  
    row 13  
    %%
</code></pre>

<p>The '%Notecard...%%' group in the 'nowis.struct' begins each '*.nc' file. <br />
The key/values (height 300, width 400) determines the size of the Notecard window and
the key/value (font_size) establishes the letter size.</p>

<pre><code>    %Notecard  
    height      300  
    width      400  
    font_size 14  
    asteriskButton  on
    priorButton     on
    %%
</code></pre>

<p>The other command tags that the ParserValidator subsystem uses in the match statement <br />
are  ('a', 'e', 'g', 'f', and 'x')  :</p>

<pre><code>    case 'a'  =&gt;  Assigner . assignerCommand(...)  
    case 'e'  =&gt;  EditCommand  .  editCommand(...)  
    case 'g'  =&gt;  GroupCommand  .   groupCommand(...)  
    case 'f'   =&gt;  NextFile  .  nextFile(...)  
    case 'x'  =&gt;  Xecute  .  xecuteCommand(...)
</code></pre>

<p>The other important role that  the ParserValidator subsystem performs is to evaluate <br />
each command and to raise an exception  when the command syntax is invalid. <br />
SyntaxException class displays the line containing the invalid syntax and a description <br />
of the error.   The 'nowis.nc' file is shown with a syntax error.  The 'd' command <br />
lacks a space following the 'd' tag:  </p>

<pre><code>    c  
    dnow is  
    c  
    d for all  
    * end
</code></pre>

<p>The  following  error message is printed:  </p>

<pre><code>:line=  
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

<pre><code>:line=  
    * emd  
error:  unknown key: emd
</code></pre>

<p>The syntax checking of this subsystem is extensive and its specifices are
not covered here.  </p>

<h2>BuildStructure  Subsystem </h2>

<p>The completion of 'ParserValidator' delivers the following List[List[String]] to
'BuildStructure' (each '%<className>...%%' is a list element of List).</p>

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

<p>The above List resembles the elements of the 'nowis.struct' file presented earlier.
On closer inspections of the two, the 'child', 'address', and 'sibling' elements
of the file are missing from the above List. </p>

<p>The following shows a scaled down version of 'nowis.struct' with only the %<className>
and the 'child', address, and 'sibling' elements</p>

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

<p>The scaled down version of 'nowis.struct' reveals the linked list structure.  A parent 
class element 'child' references the 'address' of its first child whose 'sibling' elements 
references other parent children.  For example, The parent 'Notecard' is linked to its 
three children ('CardSet's and 'NotecardTask').</p>

<p>The initial BuildStructure task is to create class instances with %<class names>. <br />
The 'nowis.struct' file has the following class names:  </p>

<pre><code>    %Notecard  
    %CardSet  
    %RowerNode  
    %DisplayText  
    %CardSet  
    %RowerNode  
    %DisplayText  
    %NotecardTask
</code></pre>

<p>BuildStructure invokes CommandLoader.createObject to instantiate objects. </p>

<pre><code>“%&lt;classname&gt; “ match {  
    case %Notecard  =&gt;   NotecardCmd( &lt;class arguments&gt;)  
    case %CardSet   =&gt;   CardSetCmd( &lt;class arguments)  
    case %RowerNode=&gt;    RowerNodeCmd( &lt;class arguments&gt;)  
    case %DisplayText=&gt;  DisplayTextCmd( &lt;class arguments&gt;)  
    case %NotecardTask=&gt; NotecardCmd( &lt;class arguments&gt; )
</code></pre>

<p>The classes,like NotecardCmd, are case classes.  The class instances are passed 
arguments.  NotecardCmd, for example, is passed the following arguments:</p>

<pre>
                height  300
                width   400
                font_size   14
                asteriskButton  on
                priorButton on    
</pre>

<p>There are 13 case classes of the type '<classname>Cmd'  representing script commands <br />
as well as components of these commands.  The above code is shown to just handles the 
simple   'nowis.nc' script.  </p>

<p>The '<classname>Cmd objects are assembled into a List of Any type.   The NotecardCmd <br />
object is at the head of this List.  Every List object is given a unique index or Id. <br />
Starting with NotecardCmd ,  it is assigned 2002 as an Id value, and each successive <br />
List objects is assigned an incremented value.   Values 2002 to 2002 + n serve as <br />
symbolic addresses of the '<classname>Cmd objects.   </p>

<h2>Structure.</h2>

<p>The final step of the Script program is to organize the '<classname>Cmd' into a <br />
structure  of linked lists where the root of this structure is NotecardCmd.   The <br />
Notecard 'card' program general approach is to iterate a series of linked lists. <br />
The following, beginning with NotecardCmd,  is the linked list structure:  </p>

<pre>
    <clsssname>Cmd  Types               Script Examples    

    NotecardCmd  
        NotecardTaskCmd                  * end    
        NextFileCmd                      f maleScript    
        CardSetCmd                       c (1)=(2)    
            AssignCmd                    a $count=$count+1    
            XNodeCmd                     x  
            GroupCmd                     g (1) = (1)  
            CardSetTask                  * continue  
            RowerNodeCmd                 d 3/5/  
                DisplayTextCmd           d now is  
                DisplayVariableCmd       d (% $count)  
                BoxFieldCmd              d (# $name)  
                    EditCmd              e ($count) < (5)  
</pre>

<p>NotecardCmd has three children types (NotecardTaskCmd, NextFileCmd, and CardSetCmd). <br />
The sibling CardSet has 6 children types.  RowerNodeCmd and BoxField are siblings as <br />
well as parent types.   </p>

<p>Our List objects of <classname>Cmd type have been assigned symbolic addresses but each <br />
object lacks  the symbolic address of the object to which it is linked.  In the case of <br />
a parent type,  the object lacks a link to its next sibling object and to its first child <br />
object.  It is the role of the parent classes (NotecardCmd, CardSetCmd, RowerNodeCmd, and <br />
BoxFieldCmd) to  establish the linkage of symbolic addresses.   </p>

<p>Each parent class instantiates  NodeParent that has two fields (firstChild, tail). <br />
firstChild references the initial child and tail references the last child added to <br />
the list.   The function that builds the linked list is Link.append(...) a trait of <br />
the parent class.  It is important to note that objects will be linked by their physical <br />
addresses.   </p>

<p>The means by which the physical addresses are linked is to take NotecardCmd off the top <br />
of the List and to pass all other <classname>Cmd  to NotecardCmd.  NotecardCmd <br />
“recognizes” it children by the following match statement:  </p>

<pre><code>    val parent = new NodeParent  
    var cardSet=null  
    …  
    c=&lt;next List element&gt;  
    c  match {  
        case nf: NextFileCmd=&gt;        Link.append(parent, nf)  
        case ft: NotecardTaskCmd=&gt;    Link.append(parent,nf)  
        case cs: CardSetCmd=&gt;         Link.append(parent, cs)  
                                      cardSet=cs  
        case _=&gt;cardSet.attach(c)
</code></pre>

<p>When a parent does not “recognize” an object as its child, then  <next List element> <br />
passed the object off  to another parent class type.  In the example, CardSet.attach(c) <br />
is invoked.  The other parent class have similar code.  In the case of an EditCmd <br />
object,  it  passes through three parent types before it is delivered to its parent, <br />
i.e., BoxFieldCmd.   </p>

<p>The translation of physical addresses to symbolic addresses is handled by the method <br />
'postIds' which is common to all <classname>Cmd classes.  The following example uses <br />
'postIds' in CardSetCmd.  </p>

<pre><code>def  postIds {  
    postChild  
    postNextSibling  
    }
</code></pre>

<p>CardSetCmd is a parent of other <classname>Cmd objects as well as a child of NotecardCmd. <br />
It must find two symbolic addresses.  It holds the reference to its first child enabling <br />
it to return child's symbolic address.  It also holds the reference to its next sibling <br />
to return the siblings symbolic address  .</p>

<p><classname>Cmd classes that are not parents need only reference the sibling that it is <br />
linked to, thus:  </p>

<pre><code>def postIds {  
    postNextSibling  
    }
</code></pre>

<p>Finally with each <classname>Cmd object in List having a symbol address, the symbolic <br />
addresses of other objects, and the argument list passes to it when created, then the <br />
List is iterated to have  each object print this information to  the '.struct' file.   </p>
