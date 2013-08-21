<h1>Script</h1>

<p>The Script program (script.scala) supports the Notecard program (see Notecard 
Respository).  The 'script' program validates the script's command syntax <br />
whose file extension is '.nc'. Its output file has the extension '.struct' 
and is the input argument to the Notecard program.</p>

<p>In the Script program, when a command syntax error is encountered, the command <br />
is displayed and a brief description of the error follows . The '.struct' <br />
file is not generated. For example, the following is displayed when the <br />
'd' command variable is missing the $ symbol (i.e., $age):  </p>

<pre><code>    line =   d Enter your age (# age)    
    ill-formed $ variable expression.
</code></pre>

<h2>Language:</h2>

<pre><code>    Scala
</code></pre>

<h2>Run example (src directory):  </h2>

<pre><code>    scala script demo/card
</code></pre>

<pre>
                             'demo' is the directory conatining 'card.nc'
</pre>

<h2>Compilation (src directory):</h2>

<pre><code>    fsc *.scala
</code></pre>
