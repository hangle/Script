<h1>Script</h1>

<p>The Script program (script.scala) supports the Notecard program, (see: Notecard <br />
Respository).  The Notecard program displays note card, size windows of text and <br />
input fields to capture the user's response.  The displays are controlled by a <br />
set of script commands.</p>

<p>The 'script' program validates the script's command-syntax whose file extension 
is '.nc'.  The program's  output file has the extension '.struct'.   The '.struct' <br />
file  is the input argument to the Notecard program (see Notecard repository).</p>

<p>In the Script program, a command syntax error causes the command line to be <br />
displayed along with  a brief description of the error. In the event of an error, <br />
the '.struct' file is not generated. For example, the following is displayed when <br />
the 'd' command variable is missing the $ symbol (i.e., $age):  </p>

<pre><code>    line =   d Enter your age (# age)    
    ill-formed $ variable expression.
</code></pre>

<h2>Language:</h2>

<pre><code>    Scala
</code></pre>

<h2>Run example (src directory):  </h2>

<p>Execution of 'script', scala's main program, executes the Script program. <br />
The 'demo' directory holds script that demonstrates the capabilities of the Notecard
commands.  The file 'card.nc' is the start file of the demonstration.</p>

<pre><code>    scala script demo/card
</code></pre>

<p>The directory 'test' contains script files that perform systematic test of the
script command types.  </p>

<h2>Compilation (src directory):</h2>

<pre><code>    fsc *.scala
</code></pre>
