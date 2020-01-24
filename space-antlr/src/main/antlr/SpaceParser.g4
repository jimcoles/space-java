/*------------------------------------------------------------------------------
 The Antlr grammar for Space if I can get it to work
------------------------------------------------------------------------------*/
parser grammar SpaceParser;

options {
    tokenVocab = SpaceLexer;
}

/*
Meta-Naming Conventions:

  __Defn : A construct that defines the structure of a type (generalization)
           or meta notion.
  __Expr : A construct that references declared elements and expresses
           some sequence of actions possibly resulting in a value at
           runtime.  Might express a mix of operations and truths
           (predicates).
           ?? Do we need ActionExpr vs EquationExpr.
  __Stmt : Expresses a thing to be executed such as a function call,
            not a structure;

Examples to prime the pump:

type MySpaceDef [equates-to OtherSpaceDef YetAnotherSpaceDef ...] (
    // Scalars
    int myIntVar [=<int>];  // var-defn
    boolean myBoolVar [=];  // var-defn

    // These define Associations (ala Foreign Key Relationships)
    MyOtherSpaceDef ref;

)

function myFunction solves <MyEquation> for [<var1>, <var2>] given [<var3>, <var4>] (

)

function myFunction solves <MyEquation> for [<var3>, <var4>] given [<var1>, <var2>] (

)

 Equations are Relations expressed symbolically, i.e., symbolic expressions.
 They are just predicates in normal form analogous to
 ax + by + cz = 0
 Every (orthogonal) Equation reduces the number of degrees of freedom (DOF) of a system by one.
equality-constraint MyEquation (

)

 A Constraint is a general predicate that must be true at the end of
 every transaction.
constraint MyConstraint (

)

 An Enumerated Relation (aka, Table, Set of Tuples) is just a set of tuples
 aligned to some type definition
enum-relation MyRelation [] (
    []
)

 Physical

index MyIndex (
)

 specifies vars of root and associated types with filter criteria
query-def <queryName> (

    '/' <rootObject> // may be a type

    [assocs
        ./<assocName> [as <alias>]
        {  // nested associations
            ./<assocName> [as <alias>]
        }
    ]

    vars
        <assocName|alias>.var1 [, ...]

    filter

        // Should/May not contain object refs, only variable refs ??

        [ ( <assocName|alias>.var <boolOper> <valueExpr> )
          <boolOper> [<...> ]

        | <namedEquation>

)

 Operators produce a new computed thing from existing thing(s)

 Navigation opers = Query opers
. - LEFT nav.  Do we need a RIGHT nav operator to tell engine that reference is in the right space?


 integer and real opers
+
==

 Boolean opers
&&
||

 Space operators
(rootSpace=MySpace1
  (Select <locallyQualified>)  // implies need joins
  (<annonymoust constraint definition> or <constraintRef>)
)

 Regex opers?  In effect, make regexes part of the language

*/

/*------------------------------------------------------------------------------
  Parse Rule Section

  If matched by the ANTLR recognizer, these will generate Rule Context nodes
  in the parse tree with the rule name specified.  Parse rules should reflect
  high-level concepts of the language.

  NOTE: Parse Rule names must start with a lowercase letter.
------------------------------------------------------------------------------*/

parseUnit :
    packageStatement?
    importStatement*
    (spaceTypeDefn | equationDefn | functionDefn | topLevelAssociationDefn)*
    ;

/*
  [  // a tuple
    space-type = "SpaceDef",
    name = "MySpace",
    sp
*/

parseUnitRelational :
    anyThing*
    ;

anyThing :
    spaceTypeDefn
    | equationDefn
    | associationDefn
    | queryDefn
    | regularExpr
    ;

packageStatement :
    'package' metaRefExpr ';';

importStatement :
    'import' aliasedSpacePathExpr ';';

spaceTypeDefn :
    accessModifier? defnTypeModifier?
    TypeDefKeyword identifier
    (ExtendsKeyword spacePathList)?
    elementDeclHeader?
    spaceTypeDefnBody
    ;

accessModifier :
    AccessKeyword
    ;

defnTypeModifier :
    SpaceDefnType
    ;

elementDeclHeader :
    comment?
    annotation+
    ;

/*
   Enforce structure:
    1. vars, then assocs, then actions.
    2. decl/defn before any initializaiton.
*/
spaceTypeDefnBody :
    BlockStart
    ( variableDefnStmt
      | keyDefnStmt
      | associationDefnStmt
      | statementBlock
      | functionDefn
      | equationDefn
      | spaceTypeDefn )*
    BlockEnd
    ;

datumDefnStmt :
    variableDefnStmt | associationDefnStmt
    ;

variableDefnStmt :
    variableDefn ';'
    ;

variableDefn :
    variableDecl rightAssignmentExpr?
    ;

variableDecl :
    elementDeclHeader? primitiveOptSeqTypeRef identifier
    ;

//associationDefnList :
//    SeqStart associationDefn (',' associationDefn)* SeqEnd
//    ;

associationDefnStmt :
    associationDefn ';'
    ;

associationDefn :
    associationDecl rightAssignmentExpr?
    ;

associationDecl :
    elementDeclHeader? complexOptCollTypeRef identifier
    ;

topLevelAssociationDefn :
    'assoc' identifier '{'
        (endAnno=annotation)*
    '}'
    ;

/*------------------------------------------------------------------------------
  KEYS and INDEXes

  Keys and Indexes have the RDB semantic.

  Keys are indentifier variables used to enforce uniqueness of tuples in a set.
  Keys are user-level identifiers as opposed to the internal Space OIDs, which
  are not accessible to the user program.

  Mathematically, I think a Key is just the 'parameter', 't', in a Parametric Equation,

    x(t), y(t)

  usually denoted by a, 't' or 'w', as opposed to 'x' or 'y'.

  A given space Type may have at most one primary key but any number of
  alternate keys.

  An Index allows for more performant (log n) lookup of objects in a Space than is
  given by a linear time (n) 'full scan' of the space. This is analogous to an index
  (keyword index) in the back of a book.

  An Index is a physical notion which may be defined independently of a
  Relation. All Keys will
------------------------------------------------------------------------------*/

keyDefnStmt :
    keyDefn ';'
    ;

keyDefn :
    elementDeclHeader?
    'key' keyModifier? identifier spacePathList
    ;

keyModifier :
    'primary' | 'alternate'
    ;

/*------------------------------------------------------------------------------
  EQUATIONS


  The term 'Equation' is used to match the mathematical notion.
  Equations are symbolic or enumerated constraints defined with respect to a
  (base) type

  An equation is a set-theoretic Relation expressed symbolically using algebra of
  other rigourously defined operators.

  A Relations is, conceptually, a  set of points (tuples)
  constituting a equality constraint on a Space as defined by a Type
  (set of variables). Evaluates to true or false (or unknown?) or 'exists'.
  Can be used to find a solution (or solutions) given independent variables,
  which may be a single object.
------------------------------------------------------------------------------*/

equationDefn :
    elementDeclHeader?
    EquationDefKeyword accessModifier? identifier
     '{' equationExpr '}'
    ;

equationExpr :
    symbolicExpr |  // type check: boolean-valued
    valueExprChain  // type check: set of tuples
    ;

/*------------------------------------------------------------------------------
  QUERY EXPRESSIONS

  Our query model, is more like object-oriented
  query model like JQL or any of the models used by persistence engines like
  Hibernate.

  A query can be:
   1. An algebraic "Projection" from (x1, x2, ...) to some subset of
    (x3, x7, ...)
    2.

  Differentiate:

      'view' - A static 'View' definition Query that describes a new Type as an algebraic variation
      of existing Types using Projection. Can be a Denormalizing Query. Mapping Query. Used
      in mapping base Types to some Interface (or other Space) or as a means of defining
      new Types or Interfaces. Used to define Type 'extension'.

      'type MyNewType extends BaseType { ... }'

        equivalent

      'view MyNewType { BaseType vars *}'

  Things to reconcile:

      Type
      Equation
      Function
      Space - An identified set of Types and related Equations (and related Functions, etc)
      Interface - Is this the same as a View?
      Query
      View

      Transform - Space, S1, to Space, S2
      Cast - Type, T1, to Type, T2

      Path Expression
      Projection (var subset)
      Selection (filter)

------------------------------------------------------------------------------*/

queryDefn :
    'query' identifier '{' queryExpr '}'
    ;

queryExpr :
    queryRootExpr
    ('assocs' aliasedSpacePathExprList)?
    ('vars' aliasedSpacePathExprList)?
    ('filter' valueExprChain)?
    ;

queryRootExpr:
    complexTypeRef
    ;

/*------------------------------------------------------------------------------
  FUNCTIONS

  A function, in its pure form, is kind of Relation, wherein there is a unique
  mapping from some independent variables to the remaining dependent variables
  in the space.
  Functions are expresssed in such a way that they provides a way to compute
  the solution to an Equation. Space functions,
  in general, compute solutions via a sequence of imperitive statements.

  Some subset of Space equations might be solvable via our inference engine,
  the Equator, which can infer an 'execution plan' that solves the equation.
  For more complex equations, the programmer will have to supply functions
  that solve for the tuple (or set of tuples) that satisfy the current
  system of equations.
------------------------------------------------------------------------------*/

functionDefn :
    elementDeclHeader?
    'function' accessModifier? (anyTypeRef | voidTypeName) identifier indParams=parameterDefnList
    ('context' contextSpace=parameterDefnList)?
    ('solves' metaRefExpr)?
    statementBlock
    ;

parameterDefnList : '(' (parameterDecl (',' parameterDecl)* )? ')' ;

parameterDecl :
    variableDecl |
    associationDecl
    ;

/*------------------------------------------------------------------------------
  SPACE PATH EXPRESSIONS

  At this point Space Paths are just paths through the AST namepace used to
  specify types. Space Paths can be followed prior to runtime with no
  dynamic evaluation.

  At some point, Space paths expressions might become more like XPath
  expressions: From a starting contextual Space object, one accesses one or
  multiple associated objects using the path expression language. Should be
  applicable to both user objects and meta objects.

  Similar to QueryExpr
  Similar to ValueExpr

  Semantically valid pairs:

    [dir].[dir]
    [dir].[type]
    [type].[assoc]
    [type].[var]
    [valueExpr].[func name ()]

    [literal of complex type].[assoc name]
    [literal of complex type].[func name ()]

    [literal of type]

    [symbolic expr (scalar-valued or tuple-valued)]

  Syntactically valid pairs:

    [identifier].[identifier]
    [identifier].[identifier ()]
    [identifier ()].[identifier]
    [identifier ()].[identifier ()]
    [object literal].[identifier]
    [object literal].[identifier ()]

  [ns]:[]

  Path Operators:

    '->' - asssociation nav (1-for-1)
    '<-' - reverse nav (many-for-1)
    '.'  - member name (tuple variable or function ref)
    '/'  - (derived) tree nav (including the AST)

------------------------------------------------------------------------------*/

metaRefExpr :
    languageKey? spacePathRootExpr? idRef (spacePathAnyNavOper idRef)*
    ;

alias :
    'as' identifier
    ;

languageKey : (idRef ':') ;

spacePathRootExpr : SPathRoot ;

spacePathAnyNavOper :
    SPathDirNavOper | SPathMemberNavOper | SPathRefNavOper
    ;

spacePathList :
    metaRefExpr (',' metaRefExpr)*
    ;

aliasedSpacePathExpr:
    metaRefExpr alias?
    ;

aliasedSpacePathExprList :
    aliasedSpacePathExpr (',' aliasedSpacePathExpr)*
    ;

anyTypeRef :
    primitiveOptSeqTypeRef
    | complexOptCollTypeRef
    ;

complexOptCollTypeRef : complexTypeRef anyCollectionMarker* ;

primitiveOptSeqTypeRef : primitiveTypeName sequenceMarker* ;

complexTypeRef :
    metaRefExpr
    ;

//collectionTypeRef :
//    (spacePathExpr | primitiveTypeName) collectionMarker
//    | collectionTypeRef collectionMarker
//    ;

anyCollectionMarker :
    setMarker
    | sequenceMarker
    ;

setMarker : '{' '}' ;

sequenceMarker : '[' ']' ;

voidTypeName :
    VoidKeyword
    ;

primitiveTypeName :
    BooleanKeyword
    | CharKeyword
    | OrdinalKeyword
    | CardinalKeyword
    | RealKeyword
    | VoidKeyword
    ;

/*------------------------------------------------------------------------------
  TREE PATHS

  A Tree Path is a declarative specification of a path through associated types
  which then forms a tree data structure. The runtime makes the resulting tree
  available to the user space. The idea is that a user should never have to
  maintain their own tree structures, which tend to require the maintenance of
  redundant referenes.

  A tree specification is very similar to an specification of XML element nestings
  or Google Protocol message nestings. One specifies the valid nestings by
  Type and the nature of the nesting in terms of 'set', 'sequence', 'choice'

  The simplest tree path is the recursive parent-child association within a
  single entitiy. E.g.,

    type Directory {
        charseq name;
        Directory parentDir;

        tree dirTree  {
            root {
                set Directory.parentDir (name);    // existing assoc
                set File.dir (name);               // existing assoc
            }
        }
    }

    type File {
        charseq name;
        Directory dir;
    }

    {
        Directory myDir = [];
        myDir.getChildren().getChildren();
        myDir.getChild(
    }

------------------------------------------------------------------------------*/

treePathExpr :

    ;

/*------------------------------------------------------------------------------
 STATEMENTS

 Statements are the imperative side of the Space langauge. They are
 execuatble instructions that allow users to implement algorithms, modify
 program state, etc..
------------------------------------------------------------------------------*/
statement :
    expression ';'
    | statementBlock
    | ifStatement
    | forEachStatement
    | returnStatement
    ;

statementBlock :
    BlockStart
    (datumDefnStmt | statement)*
    BlockEnd
    ;

ifStatement :
    'if' '(' valueExprChain ')'
    statementBlock
    ;

forEachStatement :
    'foreach' identifier 'in' valueExprChain
    statementBlock
    ;

returnStatement :
    'return' valueExprChain ';'
    ;

/*------------------------------------------------------------------------------
 VALUE EXPRESSIONS and related
------------------------------------------------------------------------------*/

// NOTE: A 'function call' may be a list of params or a single tuple
// object holding parameters.  The language runtime knows the names/paths of
// all elements in a Tuple.

// A valueExpr is like a metaRefExpr except that the latter is a simple
// dot-seperated list of identifiers, whereas the former may start
// with literals or symbolic expressions and may contain function calls

valueExprChain :
    atomicValueExpr ('.' namedRefValueExpr)*
    ;

functionCallExpr :
    idRef '(' argList ')'
    ;

argList :
    valueExprChain? ('.' valueExprChain)*
    ;

expression :
    variableDefn |
    associationDefn |
    functionCallExpr |
    assignmentExpr |
    valueExprChain
    ;

atomicValueExpr :
    literalExpr
    | tupleLiteral
    | setLiteral
    | symbolicExpr
    | namedRefValueExpr
    ;

namedRefValueExpr :
    metaRefExpr
    | functionCallExpr
    ;

//argTupleOrRef : (untypedTupleLiteral | spacePathExpr) ;


valueOrAssignmentExprList :
    valueOrAssignmentExpr (',' valueOrAssignmentExpr)*
    ;

valueOrAssignmentExpr :
    valueExprChain | assignmentExpr
    ;

/*------------------------------------------------------------------------------
  SYMBOLIC EXPRESSIONS
  A.K.A.: operator expressions

  A symbolic expression (symex) is a possibly nested group of operator-based
  expressions where each expression takes one, two, or more values. Values
  may be any valid value expression including literals, function calls, type
  var references, etc:

  (( 1 + 2 ) == 4 )

  ((A == B) AND (NOT (A > 27)))

  A 'Rule' is any boolean-valued symex.

  An 'Equality Constraint' is a symex of the form "A == (...)"
  An 'Inequality Constraint' is a symex of the form "A
------------------------------------------------------------------------------*/

symbolicExpr :
    '(' binaryOperExpr ')'
    ;

unaryOperExpr :
    unaryOper valueExprChain
    ;

unaryOper :
    BooleanUnaryOper
    ;

binaryOperExpr :
    valueExprChain binaryOper valueExprChain
    ;

binaryOper :
    NumericBinaryOper | BooleanBinaryOper | ComparisonOper
    ;


/*------------------------------------------------------------------------------
  LITERAL EXPRESSIONS

------------------------------------------------------------------------------*/

literalExpr :
    scalarLiteral
    | stringLiteral
    | tupleLiteral
    | setLiteral
    ;

scalarLiteral :
    integerLiteral
    | floatLiteral
    | booleanLiteral
    ;

stringLiteral : StringLiteral;
integerLiteral : IntegerLiteral;
floatLiteral : FloatLiteral;
booleanLiteral : BooleanLiteral;

tupleLiteral :
    anyTypeRef? tupleValueList
    ;

tupleValueList :
    TupleStart valueOrAssignmentExprList? TupleEnd
    ;

setLiteral :
    anyTypeRef? BlockStart tupleLiteral* BlockEnd
    ;

newSequenceExpr :
    anyTypeRef? '[' tupleLiteral* ']'
    ;

/*------------------------------------------------------------------------------
  Miscelleneous common little things
------------------------------------------------------------------------------*/

identifier : Identifier;

idRef : Identifier;

comment
    : singleLineComment
    | multiLineComment
    ;

singleLineComment : SingleLineComment;

multiLineComment : BlockComment;

annotation : '@' idRef '=' tupleLiteral;

rightAssignmentExpr :
    '=' valueExprChain
    ;

assignmentExpr :
    metaRefExpr rightAssignmentExpr
    ;

//rightSide :
//    literal
//    | spacePathExpr
//    ;

/*------------------------------------------------------------------------------
  GRAMMAR EXPRESSIONS
  NOTE: Possible Space Ex Language

  A sub-language for building binary stream marshallers and unmarshallers.
  Character streams are a formal subset of these.
  A grammar grammar that will help Space coders map complex strings to Spaces.
------------------------------------------------------------------------------*/

grammarExpression :

    ;

/*------------------------------------------------------------------------------
  REGULAR EXPRESSIONS
  NOTE: Possible Space Ex Language (although, it might be difficult to map
       Space grammars to the nuiances of the Regualr Expression specification.

  A sub-language for parsing industry standard regular expressions like those
  used throughout Unix, Linux, Java, XML constraints, etc..
------------------------------------------------------------------------------*/
regularExpr :
    'regex'
    ;