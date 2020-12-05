parser grammar KinokoMLParser;

options { tokenVocab=KinokoMLLexer; }

compilationUnit: SHEBANG? expression EOF;

expression:
  operand=expression DOT label=Identifier # DotExpression
  | left=expression right=expression # ApplicationExpression
  | left=expression op=(ASTERISK | SLASH | PERCENT) right=expression # MulPrecedenceExpression
  | left=expression op=(PLUS | MINUS) right=expression # AddPrecedenceExpression
  | <assoc=right> left=expression op=DOUBLE_COLON right=expression # ConsExpression
  | left=expression op=(DOUBLE_EQ | GREATER_THAN | LESSER_THAN) right=expression # EqPrecedenceExpression
  | left=expression op=(DOUBLE_AMPERSAND | DOUBLE_VERTICAL) right=expression # LogicalExpression
  | head=expression (COMMA tail+=expression)+ # TupleExpression
  | <assoc=right> head=expression (SEMI tail+=expression) # TupleExpression
  | LET REC? binder=Identifier EQ bindant=expression IN body=expression # LetExpression
  | FN param=Identifier ARROW body=expression # FnExpression
  | IF cond=expression THEN body=expression ((ELSE fallback=expression) | END) # IfExpression
  | LCBRACKET (source=expression WITH)? (labels+=Identifier EQ values+=expression SEMI)* RCBRACKET # RecordExpression
  | typeDecl # TypeDeclExpression
  | simpleExpression # SingleExpression
  ;

simpleExpression:
  Identifier # IdentifierExpression
  | Integer # IntegerExpression
  | FloatLiteral # FloatExpression
  | StringLiteral # StringExpression
  | BooleanLiteral # BooleanExpression
  | LPAREN expression RPAREN # ParenExpression
  | LPAREN RPAREN # UnitExpression
  ;

typeExpression: Identifier;

typeDeclBody:
  typeExpression # AliasTypeDeclBody
  | (ctors+=Identifier (OF typeExpression)+) # VaritnaTypeDeclBody
  ;

typeDecl: TYPE Identifier EQ typeDeclBody;
