<h1>Script Program</h1>

<p>The execution of Script's main procedure  invokes  two major subsystems:  </p>

<pre><code>    ParserValidator(filenmae.nc)  
    StructureBuilder(filename.command)
</code></pre>

<p>The script file '.nc' is  an argument  to the first subsystem.  This  subsystem's <br />
output is an '.command' file.  The '.command' file is an  input to the second <br />
subsystem.  The 'StructureBuilder' output is a '.struct' file. The '.struct' file is <br />
executed by the Notecard program to create a series of card-size windows.   </p>

<p>The following show these three files for the 'nowis.nc' file.  </p>

<pre><code>    nowis.nc (file)

    c
    d now is
    c
    d the time
    * end
</code></pre>

<p>The 'ParserValidator' output is a '.command' file that is the input to the second <br />
system.    </p>

<pre><code>    nowis.command  (file)
</code></pre>

<pre>    %Notecard       row  0             %%            style   1  
    height  550     column 0           %CardSet      size   16  
    width   450     %%                 name          column  0  
    font_size 16    %DisplayText       condition     name  TimesRoman  
    %%              style   1          %%            text  the time  
    %CardSet        size   16          %RowerNode    color black  
    name            column 0           row  0        %%  
    condition       name TimesRoman    column 0      %NotecardTask  
    %%              text   now is      %%            task   end  
    %RowerNode      color  black       %DisplayText  type  
                                                     %%
</pre>

<p>The output of the second subsystem, 'StructureBuilder'   is  the '.struct' file <br />
(see scriptUser.odt for an overview).     </p>

<pre><code>    nowis.struct   (file)
</code></pre>

<pre>    %Notecard        %DisplaText    0              0
    2002             2004           0              %%  
    550              0              0       
    16               16             %DisplayText        
    %%               0              2007            
    %CardSet         TimesRoman     0           
    2003             now is         0       
    2002             black          16          
    2005             %%             0           
    %%               %CardSet       TimesRoman  
    %RowerNode       2006           the time    
    2004             2005           black       
    2003             2008           %%          
    0                %%             %NotecardTask       
    0                %RowerNode     2008
    0                2007           0
    %%               2006           end
</pre>

<p>At this point, no explanation of the '.command' and '.struct' files is offered. <br />
Explanation begins with the  subsystem ParserEvaluator and its input file, <br />
i.e., 'nowis.nc'.</p>

<h2>ParserValidator Subsystem  </h2>

<p>The command tags of '.nowis.nc' (c, d, *),  in a match statement, selects the following <br />
class methods:  </p>

<pre><code>    case 'c' =&gt;   CardCommand . cardCommand(...)  
    case 'd' =&gt;   DisplayCommand . displayCommand(...)  
    case '*' =&gt;   AsteriskCommand . AsteriskCommand(...)
</code></pre>

<p>These three classes create  output in the '.command' file that has a  common structure:  </p>

<pre><code>    %&lt;classname&gt;  
    argument 1  
    .  
    .  
    .  
    argument n  
    %%
</code></pre>

<p>CardCommand's output for the first 'c' script  is:  </p>

<pre><code>    %CardSet  
    name  
    condition  
    %%
</code></pre>

<p>Had the  Clear command 'c' been:  </p>

<pre><code>    c  ($abc)=(John)
</code></pre>

<p>Then the output would have been:  </p>

<pre><code>    %CardSet  
    name  
    condition   ($abc)=(John)  
    %%
</code></pre>

<p>The  arguments consist of a  pair:   key/value (e.g., “condition” -> ”($abc)=(John)” ). <br />
The  key/value argument with the key 'name' is not operational (reserved for <br />
ClassSet labels).  </p>

<p>The AsteriskCommand's output for the '* end' script is:  </p>

<pre><code>    %NotecardTask  
    task    end  
    type      
    %%
</code></pre>

<p>There are two kinds of Asterisk commands  One kind  operates at the level of the CardSet, <br />
for example:  </p>

<pre><code>    * continue
</code></pre>

<p>Others operate at the Notecard level, for example:  </p>

<pre><code>    * width   400  
    * end
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

<p>The script 'd now is'  did not override Appearance default values.  The following <br />
command does so:  </p>

<pre><code>    d /color red/size 10/now is  

    %DisplayTextNotecardTask  
    style   0  
    size    10  
    column 0  
    name    TimesRoman  
    text    now is  
    color   red  
    %%
</code></pre>

<p>The Display 'd' command is  complex with a number of features.  For each 'd' command, <br />
the DisplayCommand class  creates :  </p>

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

<p>The %Notecard structure that begins the '.command' file has no corresponding tag like <br />
'c', 'd', or '*'.  </p>

<pre><code>    %Notecard  
    height      550  
    width      450  
    font_size 16  
    %%
</code></pre>

<p>Only one %Notecard structure, containing Appearance default value,  is created for the <br />
'.command' file.  </p>

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
error:  unknown key: emdNotecardTask
</code></pre>

<p>The syntax checking of this subsystem is extensive.  The Notecard prNotecardTaskogram <br />
does not do any error checking of the script passed to it.  It is the responsibility <br />
of the Script program to insure that the Notecard program runs error free. NotecardTask  </p>

<p>Build Structure Subsystem   </p>

<p>The initial task of BuildStructure is to create class objects from %<class names>. <br />
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

<p>BuildStructure invokes CommandLoader.createObject to instantiate objects.  The classes, <br />
like, NotecardCmd, are case classes.  NotecardCmd is passed arguments (550, 450, 16) <br />
as a string array. Each <classname>Cmd object stores its argument array as the variable <br />
'parameters'.  </p>

<pre><code>“%&lt;classname&gt; “ match {  
    case %Notecard  =&gt;  NotecardCmd( &lt;class arguments&gt;)  
    case %CardSet   =&gt;   CardSetCmd( &lt;class arguments)  
    case %RowerNode=&gt;RowerNodeCmd( &lt;class arguments&gt;)  
    case %DisplayText=&gt;DisplayTextCmd( &lt;class arguments&gt;)  
    case %NotecardTask=&gt;NotecardCmd( &lt;class arguments&gt; )
</code></pre>

<p>There are 15 case classes of the type '<classname>Cmd'  representing script commands <br />
or components of these commands.  The above code is shown to just handles the simple <br />
'nowis.nc' script.  </p>

<p>The '<classname>Cmd objects are assembled into a List of Any type.   The NotecardCmd <br />
object is at the head of this List.  Every List object is given a unique index or Id. <br />
Starting with NotecardCmd ,  it is assigned 2001 as an Id value, and each successive <br />
List objects is assigned an incremented value.   Values 2001 to 2001 + n serve as <br />
symbolic addresses of the '<classname>Cmd objects.   </p>

<h2>Structure.</h2>

<p>The final step of the Script program is to organize the '<classname>Cmd' into a <br />
structure  of linked lists where the root of this structure is NotecardCmd.   The <br />
Notecard 'card' program general approach is to iterate a series of linked lists. <br />
The following, beginning with NotecardCmd,  is the linked list structure:  </p>

<pre><code>&lt;clsssname&gt;Cmd  Types               Script Examples    

NotecardCmd  
    NotecardTaskCmd                 * end    
    NextFileCmd                 f maleScript    
    CardSetCmd                  c (1)=(2)    
        AssignCmd               a $count=$count+1    
        XNodeCmd                x  
        GroupCmd                g (1) = (1)  
        CardSetTask             * continue  
        RowerNodeCmd                d 3/5/  
            DisplayTextCmd          d now is  
            DisplayVariableCmd      d (% $count)  
            BoxFieldCmd         d (# $name)  
                EditCmd         e ($count) &lt; (5)
</code></pre>

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
        case ft: NotecardTaskCmd=&gt; Link.append(parent,nf)  
        case cs: CardSetCmd=&gt;         Link.append(parent, cs)  
                                cardSet=cs  
        case _=&gt;cardSet.attach(c)
</code></pre>

<p>When a parent does not “recognize” an object as its child, then  <next List element> <br />
passed the object off  to another parent class type.  In the example, CardSet.attach(c) <br />
is invoked.  The other parent class have similar code.  In the case of an EditCmd <br />
object,  it  passes through three parent types before it is “recognized” by its parent, <br />
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
