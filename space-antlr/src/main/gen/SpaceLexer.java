// Generated from /Users/jcoles/Source/jkc/space-java/space-antlr/src/main/antlr/SpaceLexer.g4 by ANTLR 4.9.1
import org.antlr.v4.runtime.Lexer;
import org.antlr.v4.runtime.CharStream;
import org.antlr.v4.runtime.Token;
import org.antlr.v4.runtime.TokenStream;
import org.antlr.v4.runtime.*;
import org.antlr.v4.runtime.atn.*;
import org.antlr.v4.runtime.dfa.DFA;
import org.antlr.v4.runtime.misc.*;

@SuppressWarnings({"all", "warnings", "unchecked", "unused", "cast"})
public class SpaceLexer extends Lexer {
	static { RuntimeMetaData.checkVersion("4.9.1", RuntimeMetaData.VERSION); }

	protected static final DFA[] _decisionToDFA;
	protected static final PredictionContextCache _sharedContextCache =
		new PredictionContextCache();
	public static final int
		BlockStart=1, BlockEnd=2, UserSeqStart=3, UserSeqEnd=4, ListSep=5, SpaceStart=6, 
		TupleStart=7, TupleEnd=8, StatementEnd=9, ColonDelim=10, AnnotationMarker=11, 
		NewOper=12, AccessKeyword=13, PackageKeyword=14, ImportKeyword=15, TypeDefKeyword=16, 
		AssocKeyword=17, ExtendsKeyword=18, EquationDefKeyword=19, FunctionDefKeyword=20, 
		ContextKeyword=21, SolvesKeyword=22, ForKeyword=23, GivenKeyword=24, FromEndKeyword=25, 
		ToEndKeyword=26, AssocKindKeyword=27, QueryDefKeyword=28, AssocsKeyword=29, 
		VarsKeyword=30, FilterKeyword=31, AsKeyword=32, KeyKeyword=33, PrimaryKeyword=34, 
		AltKeyword=35, RegexDefKeyword=36, BooleanKeyword=37, CharKeyword=38, 
		OrdinalKeyword=39, CardinalKeyword=40, RealKeyword=41, VoidKeyword=42, 
		PublicKeyword=43, IfKeyword=44, ThenKeyword=45, ForEachKeyword=46, InKeyword=47, 
		ReturnKeyword=48, SpaceDefnType=49, BooleanLiteral=50, AssignOper=51, 
		BooleanBinaryOper=52, BooleanUnaryOper=53, NumericBinaryOper=54, ComparisonOper=55, 
		SPathRoot=56, SPathDirNavOper=57, SPathMemberNavOper=58, SPathRefNavOper=59, 
		StringDelim=60, StringLiteral=61, IntegerLiteral=62, FloatLiteral=63, 
		Identifier=64, Whitespace=65, BlockComment=66, SingleLineComment=67;
	public static String[] channelNames = {
		"DEFAULT_TOKEN_CHANNEL", "HIDDEN"
	};

	public static String[] modeNames = {
		"DEFAULT_MODE"
	};

	private static String[] makeRuleNames() {
		return new String[] {
			"BlockStart", "BlockEnd", "UserSeqStart", "UserSeqEnd", "ListSep", "SpaceStart", 
			"TupleStart", "TupleEnd", "StatementEnd", "ColonDelim", "AnnotationMarker", 
			"NewOper", "AccessKeyword", "PackageKeyword", "ImportKeyword", "TypeDefKeyword", 
			"AssocKeyword", "ExtendsKeyword", "EquationDefKeyword", "FunctionDefKeyword", 
			"ContextKeyword", "SolvesKeyword", "ForKeyword", "GivenKeyword", "FromEndKeyword", 
			"ToEndKeyword", "AssocKindKeyword", "QueryDefKeyword", "AssocsKeyword", 
			"VarsKeyword", "FilterKeyword", "AsKeyword", "KeyKeyword", "PrimaryKeyword", 
			"AltKeyword", "RegexDefKeyword", "BooleanKeyword", "CharKeyword", "OrdinalKeyword", 
			"CardinalKeyword", "RealKeyword", "VoidKeyword", "PublicKeyword", "IfKeyword", 
			"ThenKeyword", "ForEachKeyword", "InKeyword", "ReturnKeyword", "SpaceDefnType", 
			"BooleanLiteral", "AssignOper", "BooleanBinaryOper", "BooleanUnaryOper", 
			"NumericBinaryOper", "ComparisonOper", "SPathRoot", "SPathDirNavOper", 
			"SPathMemberNavOper", "SPathRefNavOper", "StringDelim", "StringLiteral", 
			"IntegerLiteral", "FloatLiteral", "Identifier", "Whitespace", "Esc", 
			"Unicode", "HEX", "INT", "Exp", "IdStartChars", "IdTailChars", "BlockComment", 
			"SingleLineComment"
		};
	}
	public static final String[] ruleNames = makeRuleNames();

	private static String[] makeLiteralNames() {
		return new String[] {
			null, "'{'", "'}'", "'('", "')'", "','", "'|'", "'['", "']'", "';'", 
			"':'", "'@'", "'new'", null, "'package'", "'import'", "'type'", "'assoc'", 
			"'extends'", "'equation'", "'function'", "'context'", "'solves'", "'for'", 
			"'given'", "'from'", "'to'", "'kind'", "'query'", "'assocs'", "'vars'", 
			"'filter'", "'as'", "'key'", "'primary'", "'alternate'", "'regex'", "'boolean'", 
			"'char'", "'ord'", "'int'", "'real'", "'void'", "'public'", "'if'", "'then'", 
			"'foreach'", "'in'", "'return'", null, null, "'='", null, "'!'", null, 
			null, "'//'", "'/'", "'.'", "'->'", "'\"'"
		};
	}
	private static final String[] _LITERAL_NAMES = makeLiteralNames();
	private static String[] makeSymbolicNames() {
		return new String[] {
			null, "BlockStart", "BlockEnd", "UserSeqStart", "UserSeqEnd", "ListSep", 
			"SpaceStart", "TupleStart", "TupleEnd", "StatementEnd", "ColonDelim", 
			"AnnotationMarker", "NewOper", "AccessKeyword", "PackageKeyword", "ImportKeyword", 
			"TypeDefKeyword", "AssocKeyword", "ExtendsKeyword", "EquationDefKeyword", 
			"FunctionDefKeyword", "ContextKeyword", "SolvesKeyword", "ForKeyword", 
			"GivenKeyword", "FromEndKeyword", "ToEndKeyword", "AssocKindKeyword", 
			"QueryDefKeyword", "AssocsKeyword", "VarsKeyword", "FilterKeyword", "AsKeyword", 
			"KeyKeyword", "PrimaryKeyword", "AltKeyword", "RegexDefKeyword", "BooleanKeyword", 
			"CharKeyword", "OrdinalKeyword", "CardinalKeyword", "RealKeyword", "VoidKeyword", 
			"PublicKeyword", "IfKeyword", "ThenKeyword", "ForEachKeyword", "InKeyword", 
			"ReturnKeyword", "SpaceDefnType", "BooleanLiteral", "AssignOper", "BooleanBinaryOper", 
			"BooleanUnaryOper", "NumericBinaryOper", "ComparisonOper", "SPathRoot", 
			"SPathDirNavOper", "SPathMemberNavOper", "SPathRefNavOper", "StringDelim", 
			"StringLiteral", "IntegerLiteral", "FloatLiteral", "Identifier", "Whitespace", 
			"BlockComment", "SingleLineComment"
		};
	}
	private static final String[] _SYMBOLIC_NAMES = makeSymbolicNames();
	public static final Vocabulary VOCABULARY = new VocabularyImpl(_LITERAL_NAMES, _SYMBOLIC_NAMES);

	/**
	 * @deprecated Use {@link #VOCABULARY} instead.
	 */
	@Deprecated
	public static final String[] tokenNames;
	static {
		tokenNames = new String[_SYMBOLIC_NAMES.length];
		for (int i = 0; i < tokenNames.length; i++) {
			tokenNames[i] = VOCABULARY.getLiteralName(i);
			if (tokenNames[i] == null) {
				tokenNames[i] = VOCABULARY.getSymbolicName(i);
			}

			if (tokenNames[i] == null) {
				tokenNames[i] = "<INVALID>";
			}
		}
	}

	@Override
	@Deprecated
	public String[] getTokenNames() {
		return tokenNames;
	}

	@Override

	public Vocabulary getVocabulary() {
		return VOCABULARY;
	}


	public SpaceLexer(CharStream input) {
		super(input);
		_interp = new LexerATNSimulator(this,_ATN,_decisionToDFA,_sharedContextCache);
	}

	@Override
	public String getGrammarFileName() { return "SpaceLexer.g4"; }

	@Override
	public String[] getRuleNames() { return ruleNames; }

	@Override
	public String getSerializedATN() { return _serializedATN; }

	@Override
	public String[] getChannelNames() { return channelNames; }

	@Override
	public String[] getModeNames() { return modeNames; }

	@Override
	public ATN getATN() { return _ATN; }

	public static final String _serializedATN =
		"\3\u608b\ua72a\u8133\ub9ed\u417c\u3be7\u7786\u5964\2E\u0237\b\1\4\2\t"+
		"\2\4\3\t\3\4\4\t\4\4\5\t\5\4\6\t\6\4\7\t\7\4\b\t\b\4\t\t\t\4\n\t\n\4\13"+
		"\t\13\4\f\t\f\4\r\t\r\4\16\t\16\4\17\t\17\4\20\t\20\4\21\t\21\4\22\t\22"+
		"\4\23\t\23\4\24\t\24\4\25\t\25\4\26\t\26\4\27\t\27\4\30\t\30\4\31\t\31"+
		"\4\32\t\32\4\33\t\33\4\34\t\34\4\35\t\35\4\36\t\36\4\37\t\37\4 \t \4!"+
		"\t!\4\"\t\"\4#\t#\4$\t$\4%\t%\4&\t&\4\'\t\'\4(\t(\4)\t)\4*\t*\4+\t+\4"+
		",\t,\4-\t-\4.\t.\4/\t/\4\60\t\60\4\61\t\61\4\62\t\62\4\63\t\63\4\64\t"+
		"\64\4\65\t\65\4\66\t\66\4\67\t\67\48\t8\49\t9\4:\t:\4;\t;\4<\t<\4=\t="+
		"\4>\t>\4?\t?\4@\t@\4A\tA\4B\tB\4C\tC\4D\tD\4E\tE\4F\tF\4G\tG\4H\tH\4I"+
		"\tI\4J\tJ\4K\tK\3\2\3\2\3\3\3\3\3\4\3\4\3\5\3\5\3\6\3\6\3\7\3\7\3\b\3"+
		"\b\3\t\3\t\3\n\3\n\3\13\3\13\3\f\3\f\3\r\3\r\3\r\3\r\3\16\3\16\3\17\3"+
		"\17\3\17\3\17\3\17\3\17\3\17\3\17\3\20\3\20\3\20\3\20\3\20\3\20\3\20\3"+
		"\21\3\21\3\21\3\21\3\21\3\22\3\22\3\22\3\22\3\22\3\22\3\23\3\23\3\23\3"+
		"\23\3\23\3\23\3\23\3\23\3\24\3\24\3\24\3\24\3\24\3\24\3\24\3\24\3\24\3"+
		"\25\3\25\3\25\3\25\3\25\3\25\3\25\3\25\3\25\3\26\3\26\3\26\3\26\3\26\3"+
		"\26\3\26\3\26\3\27\3\27\3\27\3\27\3\27\3\27\3\27\3\30\3\30\3\30\3\30\3"+
		"\31\3\31\3\31\3\31\3\31\3\31\3\32\3\32\3\32\3\32\3\32\3\33\3\33\3\33\3"+
		"\34\3\34\3\34\3\34\3\34\3\35\3\35\3\35\3\35\3\35\3\35\3\36\3\36\3\36\3"+
		"\36\3\36\3\36\3\36\3\37\3\37\3\37\3\37\3\37\3 \3 \3 \3 \3 \3 \3 \3!\3"+
		"!\3!\3\"\3\"\3\"\3\"\3#\3#\3#\3#\3#\3#\3#\3#\3$\3$\3$\3$\3$\3$\3$\3$\3"+
		"$\3$\3%\3%\3%\3%\3%\3%\3&\3&\3&\3&\3&\3&\3&\3&\3\'\3\'\3\'\3\'\3\'\3("+
		"\3(\3(\3(\3)\3)\3)\3)\3*\3*\3*\3*\3*\3+\3+\3+\3+\3+\3,\3,\3,\3,\3,\3,"+
		"\3,\3-\3-\3-\3.\3.\3.\3.\3.\3/\3/\3/\3/\3/\3/\3/\3/\3\60\3\60\3\60\3\61"+
		"\3\61\3\61\3\61\3\61\3\61\3\61\3\62\3\62\3\62\3\62\3\62\3\62\3\62\3\62"+
		"\3\62\3\62\3\62\3\62\3\62\3\62\3\62\3\62\3\62\3\62\3\62\3\62\3\62\3\62"+
		"\3\62\3\62\3\62\5\62\u019f\n\62\3\63\3\63\3\63\3\63\3\63\3\63\3\63\3\63"+
		"\3\63\5\63\u01aa\n\63\3\64\3\64\3\65\3\65\3\65\3\65\3\65\5\65\u01b3\n"+
		"\65\3\66\3\66\3\67\3\67\38\38\38\58\u01bc\n8\39\39\39\3:\3:\3;\3;\3<\3"+
		"<\3<\3=\3=\3>\3>\3>\3>\7>\u01ce\n>\f>\16>\u01d1\13>\3>\3>\3?\3?\3@\5@"+
		"\u01d8\n@\3@\3@\3@\3@\5@\u01de\n@\3@\5@\u01e1\n@\3@\3@\3@\3@\5@\u01e7"+
		"\n@\3@\5@\u01ea\n@\3A\3A\7A\u01ee\nA\fA\16A\u01f1\13A\3B\6B\u01f4\nB\r"+
		"B\16B\u01f5\3B\3B\3C\3C\3C\5C\u01fd\nC\3D\3D\3D\3D\3D\3D\3E\3E\3F\3F\3"+
		"F\7F\u020a\nF\fF\16F\u020d\13F\5F\u020f\nF\3G\3G\5G\u0213\nG\3G\3G\3H"+
		"\3H\3I\3I\5I\u021b\nI\3J\3J\3J\3J\7J\u0221\nJ\fJ\16J\u0224\13J\3J\3J\3"+
		"J\3J\3J\3K\3K\3K\3K\7K\u022f\nK\fK\16K\u0232\13K\3K\3K\3K\3K\3\u0222\2"+
		"L\3\3\5\4\7\5\t\6\13\7\r\b\17\t\21\n\23\13\25\f\27\r\31\16\33\17\35\20"+
		"\37\21!\22#\23%\24\'\25)\26+\27-\30/\31\61\32\63\33\65\34\67\359\36;\37"+
		"= ?!A\"C#E$G%I&K\'M(O)Q*S+U,W-Y.[/]\60_\61a\62c\63e\64g\65i\66k\67m8o"+
		"9q:s;u<w=y>{?}@\177A\u0081B\u0083C\u0085\2\u0087\2\u0089\2\u008b\2\u008d"+
		"\2\u008f\2\u0091\2\u0093D\u0095E\3\2\20\4\2((~~\5\2,-//\61\61\4\2>>@@"+
		"\3\2$$\5\2\13\f\17\17\"\"\n\2$$\61\61^^ddhhppttvv\5\2\62;CHch\3\2\63;"+
		"\3\2\62;\4\2GGgg\4\2--//\5\2C\\aac|\4\2//\62;\4\2\f\f\17\17\2\u0247\2"+
		"\3\3\2\2\2\2\5\3\2\2\2\2\7\3\2\2\2\2\t\3\2\2\2\2\13\3\2\2\2\2\r\3\2\2"+
		"\2\2\17\3\2\2\2\2\21\3\2\2\2\2\23\3\2\2\2\2\25\3\2\2\2\2\27\3\2\2\2\2"+
		"\31\3\2\2\2\2\33\3\2\2\2\2\35\3\2\2\2\2\37\3\2\2\2\2!\3\2\2\2\2#\3\2\2"+
		"\2\2%\3\2\2\2\2\'\3\2\2\2\2)\3\2\2\2\2+\3\2\2\2\2-\3\2\2\2\2/\3\2\2\2"+
		"\2\61\3\2\2\2\2\63\3\2\2\2\2\65\3\2\2\2\2\67\3\2\2\2\29\3\2\2\2\2;\3\2"+
		"\2\2\2=\3\2\2\2\2?\3\2\2\2\2A\3\2\2\2\2C\3\2\2\2\2E\3\2\2\2\2G\3\2\2\2"+
		"\2I\3\2\2\2\2K\3\2\2\2\2M\3\2\2\2\2O\3\2\2\2\2Q\3\2\2\2\2S\3\2\2\2\2U"+
		"\3\2\2\2\2W\3\2\2\2\2Y\3\2\2\2\2[\3\2\2\2\2]\3\2\2\2\2_\3\2\2\2\2a\3\2"+
		"\2\2\2c\3\2\2\2\2e\3\2\2\2\2g\3\2\2\2\2i\3\2\2\2\2k\3\2\2\2\2m\3\2\2\2"+
		"\2o\3\2\2\2\2q\3\2\2\2\2s\3\2\2\2\2u\3\2\2\2\2w\3\2\2\2\2y\3\2\2\2\2{"+
		"\3\2\2\2\2}\3\2\2\2\2\177\3\2\2\2\2\u0081\3\2\2\2\2\u0083\3\2\2\2\2\u0093"+
		"\3\2\2\2\2\u0095\3\2\2\2\3\u0097\3\2\2\2\5\u0099\3\2\2\2\7\u009b\3\2\2"+
		"\2\t\u009d\3\2\2\2\13\u009f\3\2\2\2\r\u00a1\3\2\2\2\17\u00a3\3\2\2\2\21"+
		"\u00a5\3\2\2\2\23\u00a7\3\2\2\2\25\u00a9\3\2\2\2\27\u00ab\3\2\2\2\31\u00ad"+
		"\3\2\2\2\33\u00b1\3\2\2\2\35\u00b3\3\2\2\2\37\u00bb\3\2\2\2!\u00c2\3\2"+
		"\2\2#\u00c7\3\2\2\2%\u00cd\3\2\2\2\'\u00d5\3\2\2\2)\u00de\3\2\2\2+\u00e7"+
		"\3\2\2\2-\u00ef\3\2\2\2/\u00f6\3\2\2\2\61\u00fa\3\2\2\2\63\u0100\3\2\2"+
		"\2\65\u0105\3\2\2\2\67\u0108\3\2\2\29\u010d\3\2\2\2;\u0113\3\2\2\2=\u011a"+
		"\3\2\2\2?\u011f\3\2\2\2A\u0126\3\2\2\2C\u0129\3\2\2\2E\u012d\3\2\2\2G"+
		"\u0135\3\2\2\2I\u013f\3\2\2\2K\u0145\3\2\2\2M\u014d\3\2\2\2O\u0152\3\2"+
		"\2\2Q\u0156\3\2\2\2S\u015a\3\2\2\2U\u015f\3\2\2\2W\u0164\3\2\2\2Y\u016b"+
		"\3\2\2\2[\u016e\3\2\2\2]\u0173\3\2\2\2_\u017b\3\2\2\2a\u017e\3\2\2\2c"+
		"\u019e\3\2\2\2e\u01a9\3\2\2\2g\u01ab\3\2\2\2i\u01b2\3\2\2\2k\u01b4\3\2"+
		"\2\2m\u01b6\3\2\2\2o\u01bb\3\2\2\2q\u01bd\3\2\2\2s\u01c0\3\2\2\2u\u01c2"+
		"\3\2\2\2w\u01c4\3\2\2\2y\u01c7\3\2\2\2{\u01c9\3\2\2\2}\u01d4\3\2\2\2\177"+
		"\u01e9\3\2\2\2\u0081\u01eb\3\2\2\2\u0083\u01f3\3\2\2\2\u0085\u01f9\3\2"+
		"\2\2\u0087\u01fe\3\2\2\2\u0089\u0204\3\2\2\2\u008b\u020e\3\2\2\2\u008d"+
		"\u0210\3\2\2\2\u008f\u0216\3\2\2\2\u0091\u021a\3\2\2\2\u0093\u021c\3\2"+
		"\2\2\u0095\u022a\3\2\2\2\u0097\u0098\7}\2\2\u0098\4\3\2\2\2\u0099\u009a"+
		"\7\177\2\2\u009a\6\3\2\2\2\u009b\u009c\7*\2\2\u009c\b\3\2\2\2\u009d\u009e"+
		"\7+\2\2\u009e\n\3\2\2\2\u009f\u00a0\7.\2\2\u00a0\f\3\2\2\2\u00a1\u00a2"+
		"\7~\2\2\u00a2\16\3\2\2\2\u00a3\u00a4\7]\2\2\u00a4\20\3\2\2\2\u00a5\u00a6"+
		"\7_\2\2\u00a6\22\3\2\2\2\u00a7\u00a8\7=\2\2\u00a8\24\3\2\2\2\u00a9\u00aa"+
		"\7<\2\2\u00aa\26\3\2\2\2\u00ab\u00ac\7B\2\2\u00ac\30\3\2\2\2\u00ad\u00ae"+
		"\7p\2\2\u00ae\u00af\7g\2\2\u00af\u00b0\7y\2\2\u00b0\32\3\2\2\2\u00b1\u00b2"+
		"\5W,\2\u00b2\34\3\2\2\2\u00b3\u00b4\7r\2\2\u00b4\u00b5\7c\2\2\u00b5\u00b6"+
		"\7e\2\2\u00b6\u00b7\7m\2\2\u00b7\u00b8\7c\2\2\u00b8\u00b9\7i\2\2\u00b9"+
		"\u00ba\7g\2\2\u00ba\36\3\2\2\2\u00bb\u00bc\7k\2\2\u00bc\u00bd\7o\2\2\u00bd"+
		"\u00be\7r\2\2\u00be\u00bf\7q\2\2\u00bf\u00c0\7t\2\2\u00c0\u00c1\7v\2\2"+
		"\u00c1 \3\2\2\2\u00c2\u00c3\7v\2\2\u00c3\u00c4\7{\2\2\u00c4\u00c5\7r\2"+
		"\2\u00c5\u00c6\7g\2\2\u00c6\"\3\2\2\2\u00c7\u00c8\7c\2\2\u00c8\u00c9\7"+
		"u\2\2\u00c9\u00ca\7u\2\2\u00ca\u00cb\7q\2\2\u00cb\u00cc\7e\2\2\u00cc$"+
		"\3\2\2\2\u00cd\u00ce\7g\2\2\u00ce\u00cf\7z\2\2\u00cf\u00d0\7v\2\2\u00d0"+
		"\u00d1\7g\2\2\u00d1\u00d2\7p\2\2\u00d2\u00d3\7f\2\2\u00d3\u00d4\7u\2\2"+
		"\u00d4&\3\2\2\2\u00d5\u00d6\7g\2\2\u00d6\u00d7\7s\2\2\u00d7\u00d8\7w\2"+
		"\2\u00d8\u00d9\7c\2\2\u00d9\u00da\7v\2\2\u00da\u00db\7k\2\2\u00db\u00dc"+
		"\7q\2\2\u00dc\u00dd\7p\2\2\u00dd(\3\2\2\2\u00de\u00df\7h\2\2\u00df\u00e0"+
		"\7w\2\2\u00e0\u00e1\7p\2\2\u00e1\u00e2\7e\2\2\u00e2\u00e3\7v\2\2\u00e3"+
		"\u00e4\7k\2\2\u00e4\u00e5\7q\2\2\u00e5\u00e6\7p\2\2\u00e6*\3\2\2\2\u00e7"+
		"\u00e8\7e\2\2\u00e8\u00e9\7q\2\2\u00e9\u00ea\7p\2\2\u00ea\u00eb\7v\2\2"+
		"\u00eb\u00ec\7g\2\2\u00ec\u00ed\7z\2\2\u00ed\u00ee\7v\2\2\u00ee,\3\2\2"+
		"\2\u00ef\u00f0\7u\2\2\u00f0\u00f1\7q\2\2\u00f1\u00f2\7n\2\2\u00f2\u00f3"+
		"\7x\2\2\u00f3\u00f4\7g\2\2\u00f4\u00f5\7u\2\2\u00f5.\3\2\2\2\u00f6\u00f7"+
		"\7h\2\2\u00f7\u00f8\7q\2\2\u00f8\u00f9\7t\2\2\u00f9\60\3\2\2\2\u00fa\u00fb"+
		"\7i\2\2\u00fb\u00fc\7k\2\2\u00fc\u00fd\7x\2\2\u00fd\u00fe\7g\2\2\u00fe"+
		"\u00ff\7p\2\2\u00ff\62\3\2\2\2\u0100\u0101\7h\2\2\u0101\u0102\7t\2\2\u0102"+
		"\u0103\7q\2\2\u0103\u0104\7o\2\2\u0104\64\3\2\2\2\u0105\u0106\7v\2\2\u0106"+
		"\u0107\7q\2\2\u0107\66\3\2\2\2\u0108\u0109\7m\2\2\u0109\u010a\7k\2\2\u010a"+
		"\u010b\7p\2\2\u010b\u010c\7f\2\2\u010c8\3\2\2\2\u010d\u010e\7s\2\2\u010e"+
		"\u010f\7w\2\2\u010f\u0110\7g\2\2\u0110\u0111\7t\2\2\u0111\u0112\7{\2\2"+
		"\u0112:\3\2\2\2\u0113\u0114\7c\2\2\u0114\u0115\7u\2\2\u0115\u0116\7u\2"+
		"\2\u0116\u0117\7q\2\2\u0117\u0118\7e\2\2\u0118\u0119\7u\2\2\u0119<\3\2"+
		"\2\2\u011a\u011b\7x\2\2\u011b\u011c\7c\2\2\u011c\u011d\7t\2\2\u011d\u011e"+
		"\7u\2\2\u011e>\3\2\2\2\u011f\u0120\7h\2\2\u0120\u0121\7k\2\2\u0121\u0122"+
		"\7n\2\2\u0122\u0123\7v\2\2\u0123\u0124\7g\2\2\u0124\u0125\7t\2\2\u0125"+
		"@\3\2\2\2\u0126\u0127\7c\2\2\u0127\u0128\7u\2\2\u0128B\3\2\2\2\u0129\u012a"+
		"\7m\2\2\u012a\u012b\7g\2\2\u012b\u012c\7{\2\2\u012cD\3\2\2\2\u012d\u012e"+
		"\7r\2\2\u012e\u012f\7t\2\2\u012f\u0130\7k\2\2\u0130\u0131\7o\2\2\u0131"+
		"\u0132\7c\2\2\u0132\u0133\7t\2\2\u0133\u0134\7{\2\2\u0134F\3\2\2\2\u0135"+
		"\u0136\7c\2\2\u0136\u0137\7n\2\2\u0137\u0138\7v\2\2\u0138\u0139\7g\2\2"+
		"\u0139\u013a\7t\2\2\u013a\u013b\7p\2\2\u013b\u013c\7c\2\2\u013c\u013d"+
		"\7v\2\2\u013d\u013e\7g\2\2\u013eH\3\2\2\2\u013f\u0140\7t\2\2\u0140\u0141"+
		"\7g\2\2\u0141\u0142\7i\2\2\u0142\u0143\7g\2\2\u0143\u0144\7z\2\2\u0144"+
		"J\3\2\2\2\u0145\u0146\7d\2\2\u0146\u0147\7q\2\2\u0147\u0148\7q\2\2\u0148"+
		"\u0149\7n\2\2\u0149\u014a\7g\2\2\u014a\u014b\7c\2\2\u014b\u014c\7p\2\2"+
		"\u014cL\3\2\2\2\u014d\u014e\7e\2\2\u014e\u014f\7j\2\2\u014f\u0150\7c\2"+
		"\2\u0150\u0151\7t\2\2\u0151N\3\2\2\2\u0152\u0153\7q\2\2\u0153\u0154\7"+
		"t\2\2\u0154\u0155\7f\2\2\u0155P\3\2\2\2\u0156\u0157\7k\2\2\u0157\u0158"+
		"\7p\2\2\u0158\u0159\7v\2\2\u0159R\3\2\2\2\u015a\u015b\7t\2\2\u015b\u015c"+
		"\7g\2\2\u015c\u015d\7c\2\2\u015d\u015e\7n\2\2\u015eT\3\2\2\2\u015f\u0160"+
		"\7x\2\2\u0160\u0161\7q\2\2\u0161\u0162\7k\2\2\u0162\u0163\7f\2\2\u0163"+
		"V\3\2\2\2\u0164\u0165\7r\2\2\u0165\u0166\7w\2\2\u0166\u0167\7d\2\2\u0167"+
		"\u0168\7n\2\2\u0168\u0169\7k\2\2\u0169\u016a\7e\2\2\u016aX\3\2\2\2\u016b"+
		"\u016c\7k\2\2\u016c\u016d\7h\2\2\u016dZ\3\2\2\2\u016e\u016f\7v\2\2\u016f"+
		"\u0170\7j\2\2\u0170\u0171\7g\2\2\u0171\u0172\7p\2\2\u0172\\\3\2\2\2\u0173"+
		"\u0174\7h\2\2\u0174\u0175\7q\2\2\u0175\u0176\7t\2\2\u0176\u0177\7g\2\2"+
		"\u0177\u0178\7c\2\2\u0178\u0179\7e\2\2\u0179\u017a\7j\2\2\u017a^\3\2\2"+
		"\2\u017b\u017c\7k\2\2\u017c\u017d\7p\2\2\u017d`\3\2\2\2\u017e\u017f\7"+
		"t\2\2\u017f\u0180\7g\2\2\u0180\u0181\7v\2\2\u0181\u0182\7w\2\2\u0182\u0183"+
		"\7t\2\2\u0183\u0184\7p\2\2\u0184b\3\2\2\2\u0185\u0186\7c\2\2\u0186\u0187"+
		"\7d\2\2\u0187\u0188\7u\2\2\u0188\u0189\7v\2\2\u0189\u018a\7t\2\2\u018a"+
		"\u018b\7c\2\2\u018b\u018c\7e\2\2\u018c\u019f\7v\2\2\u018d\u018e\7d\2\2"+
		"\u018e\u018f\7c\2\2\u018f\u0190\7u\2\2\u0190\u0191\7k\2\2\u0191\u019f"+
		"\7u\2\2\u0192\u0193\7f\2\2\u0193\u0194\7g\2\2\u0194\u0195\7r\2\2\u0195"+
		"\u0196\7g\2\2\u0196\u0197\7p\2\2\u0197\u019f\7f\2\2\u0198\u0199\7p\2\2"+
		"\u0199\u019a\7c\2\2\u019a\u019b\7v\2\2\u019b\u019c\7k\2\2\u019c\u019d"+
		"\7x\2\2\u019d\u019f\7g\2\2\u019e\u0185\3\2\2\2\u019e\u018d\3\2\2\2\u019e"+
		"\u0192\3\2\2\2\u019e\u0198\3\2\2\2\u019fd\3\2\2\2\u01a0\u01a1\7v\2\2\u01a1"+
		"\u01a2\7t\2\2\u01a2\u01a3\7w\2\2\u01a3\u01aa\7g\2\2\u01a4\u01a5\7h\2\2"+
		"\u01a5\u01a6\7c\2\2\u01a6\u01a7\7n\2\2\u01a7\u01a8\7u\2\2\u01a8\u01aa"+
		"\7g\2\2\u01a9\u01a0\3\2\2\2\u01a9\u01a4\3\2\2\2\u01aaf\3\2\2\2\u01ab\u01ac"+
		"\7?\2\2\u01ach\3\2\2\2\u01ad\u01b3\t\2\2\2\u01ae\u01af\7(\2\2\u01af\u01b3"+
		"\7(\2\2\u01b0\u01b1\7~\2\2\u01b1\u01b3\7~\2\2\u01b2\u01ad\3\2\2\2\u01b2"+
		"\u01ae\3\2\2\2\u01b2\u01b0\3\2\2\2\u01b3j\3\2\2\2\u01b4\u01b5\7#\2\2\u01b5"+
		"l\3\2\2\2\u01b6\u01b7\t\3\2\2\u01b7n\3\2\2\2\u01b8\u01b9\7?\2\2\u01b9"+
		"\u01bc\7?\2\2\u01ba\u01bc\t\4\2\2\u01bb\u01b8\3\2\2\2\u01bb\u01ba\3\2"+
		"\2\2\u01bcp\3\2\2\2\u01bd\u01be\7\61\2\2\u01be\u01bf\7\61\2\2\u01bfr\3"+
		"\2\2\2\u01c0\u01c1\7\61\2\2\u01c1t\3\2\2\2\u01c2\u01c3\7\60\2\2\u01c3"+
		"v\3\2\2\2\u01c4\u01c5\7/\2\2\u01c5\u01c6\7@\2\2\u01c6x\3\2\2\2\u01c7\u01c8"+
		"\7$\2\2\u01c8z\3\2\2\2\u01c9\u01cf\5y=\2\u01ca\u01ce\n\5\2\2\u01cb\u01cc"+
		"\7^\2\2\u01cc\u01ce\7$\2\2\u01cd\u01ca\3\2\2\2\u01cd\u01cb\3\2\2\2\u01ce"+
		"\u01d1\3\2\2\2\u01cf\u01cd\3\2\2\2\u01cf\u01d0\3\2\2\2\u01d0\u01d2\3\2"+
		"\2\2\u01d1\u01cf\3\2\2\2\u01d2\u01d3\5y=\2\u01d3|\3\2\2\2\u01d4\u01d5"+
		"\5\u008bF\2\u01d5~\3\2\2\2\u01d6\u01d8\7/\2\2\u01d7\u01d6\3\2\2\2\u01d7"+
		"\u01d8\3\2\2\2\u01d8\u01d9\3\2\2\2\u01d9\u01da\5\u008bF\2\u01da\u01db"+
		"\7\60\2\2\u01db\u01dd\5\u008bF\2\u01dc\u01de\5\u008dG\2\u01dd\u01dc\3"+
		"\2\2\2\u01dd\u01de\3\2\2\2\u01de\u01ea\3\2\2\2\u01df\u01e1\7/\2\2\u01e0"+
		"\u01df\3\2\2\2\u01e0\u01e1\3\2\2\2\u01e1\u01e2\3\2\2\2\u01e2\u01e3\5\u008b"+
		"F\2\u01e3\u01e4\5\u008dG\2\u01e4\u01ea\3\2\2\2\u01e5\u01e7\7/\2\2\u01e6"+
		"\u01e5\3\2\2\2\u01e6\u01e7\3\2\2\2\u01e7\u01e8\3\2\2\2\u01e8\u01ea\5\u008b"+
		"F\2\u01e9\u01d7\3\2\2\2\u01e9\u01e0\3\2\2\2\u01e9\u01e6\3\2\2\2\u01ea"+
		"\u0080\3\2\2\2\u01eb\u01ef\5\u008fH\2\u01ec\u01ee\5\u0091I\2\u01ed\u01ec"+
		"\3\2\2\2\u01ee\u01f1\3\2\2\2\u01ef\u01ed\3\2\2\2\u01ef\u01f0\3\2\2\2\u01f0"+
		"\u0082\3\2\2\2\u01f1\u01ef\3\2\2\2\u01f2\u01f4\t\6\2\2\u01f3\u01f2\3\2"+
		"\2\2\u01f4\u01f5\3\2\2\2\u01f5\u01f3\3\2\2\2\u01f5\u01f6\3\2\2\2\u01f6"+
		"\u01f7\3\2\2\2\u01f7\u01f8\bB\2\2\u01f8\u0084\3\2\2\2\u01f9\u01fc\7^\2"+
		"\2\u01fa\u01fd\t\7\2\2\u01fb\u01fd\5\u0087D\2\u01fc\u01fa\3\2\2\2\u01fc"+
		"\u01fb\3\2\2\2\u01fd\u0086\3\2\2\2\u01fe\u01ff\7w\2\2\u01ff\u0200\5\u0089"+
		"E\2\u0200\u0201\5\u0089E\2\u0201\u0202\5\u0089E\2\u0202\u0203\5\u0089"+
		"E\2\u0203\u0088\3\2\2\2\u0204\u0205\t\b\2\2\u0205\u008a\3\2\2\2\u0206"+
		"\u020f\7\62\2\2\u0207\u020b\t\t\2\2\u0208\u020a\t\n\2\2\u0209\u0208\3"+
		"\2\2\2\u020a\u020d\3\2\2\2\u020b\u0209\3\2\2\2\u020b\u020c\3\2\2\2\u020c"+
		"\u020f\3\2\2\2\u020d\u020b\3\2\2\2\u020e\u0206\3\2\2\2\u020e\u0207\3\2"+
		"\2\2\u020f\u008c\3\2\2\2\u0210\u0212\t\13\2\2\u0211\u0213\t\f\2\2\u0212"+
		"\u0211\3\2\2\2\u0212\u0213\3\2\2\2\u0213\u0214\3\2\2\2\u0214\u0215\5\u008b"+
		"F\2\u0215\u008e\3\2\2\2\u0216\u0217\t\r\2\2\u0217\u0090\3\2\2\2\u0218"+
		"\u021b\5\u008fH\2\u0219\u021b\t\16\2\2\u021a\u0218\3\2\2\2\u021a\u0219"+
		"\3\2\2\2\u021b\u0092\3\2\2\2\u021c\u021d\7\61\2\2\u021d\u021e\7,\2\2\u021e"+
		"\u0222\3\2\2\2\u021f\u0221\13\2\2\2\u0220\u021f\3\2\2\2\u0221\u0224\3"+
		"\2\2\2\u0222\u0223\3\2\2\2\u0222\u0220\3\2\2\2\u0223\u0225\3\2\2\2\u0224"+
		"\u0222\3\2\2\2\u0225\u0226\7,\2\2\u0226\u0227\7\61\2\2\u0227\u0228\3\2"+
		"\2\2\u0228\u0229\bJ\2\2\u0229\u0094\3\2\2\2\u022a\u022b\7\61\2\2\u022b"+
		"\u022c\7\61\2\2\u022c\u0230\3\2\2\2\u022d\u022f\n\17\2\2\u022e\u022d\3"+
		"\2\2\2\u022f\u0232\3\2\2\2\u0230\u022e\3\2\2\2\u0230\u0231\3\2\2\2\u0231"+
		"\u0233\3\2\2\2\u0232\u0230\3\2\2\2\u0233\u0234\t\17\2\2\u0234\u0235\3"+
		"\2\2\2\u0235\u0236\bK\2\2\u0236\u0096\3\2\2\2\27\2\u019e\u01a9\u01b2\u01bb"+
		"\u01cd\u01cf\u01d7\u01dd\u01e0\u01e6\u01e9\u01ef\u01f5\u01fc\u020b\u020e"+
		"\u0212\u021a\u0222\u0230\3\b\2\2";
	public static final ATN _ATN =
		new ATNDeserializer().deserialize(_serializedATN.toCharArray());
	static {
		_decisionToDFA = new DFA[_ATN.getNumberOfDecisions()];
		for (int i = 0; i < _ATN.getNumberOfDecisions(); i++) {
			_decisionToDFA[i] = new DFA(_ATN.getDecisionState(i), i);
		}
	}
}