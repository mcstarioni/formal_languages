grammar first;
NUMBER:[0-9]+('.'[0-9]*)?|'.'[0-9]+;
WHITESPACE:[ \n\r\t]+ ->skip;
ADD:'+';
SUB:'-';
MUL:'*';
DIV:'/';
expr:add(('+'|'-')add)*;
add:mul(('*'|'/')mul)*;
mul:NUMBER|'('expr')';