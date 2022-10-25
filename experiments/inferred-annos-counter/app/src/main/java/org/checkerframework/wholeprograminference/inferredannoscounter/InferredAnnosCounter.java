package org.checkerframework.wholeprograminference.inferredannoscounter;

import com.github.difflib.DiffUtils;
import com.github.difflib.patch.AbstractDelta;
import com.github.difflib.patch.Patch;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import org.checkerframework.checker.index.qual.IndexFor;
import org.checkerframework.checker.index.qual.LTEqLengthOf;
import org.checkerframework.checker.index.qual.NonNegative;
import org.checkerframework.checker.nullness.qual.NonNull;
import org.checkerframework.common.value.qual.MinLen;

/**
 * The entry point for the inferred annos counter. Pass it a list of files, as detailed in README.md
 * at the top-level of this source tree.
 */
public class InferredAnnosCounter {
  private static String checkerFramworkPackage =
      "A2F A2FReducer AbstractAnalysis AbstractAnalysis.Worklist AbstractAtmComboVisitor AbstractCFGVisualizer AbstractCFGVisualizer.VisualizeWhere AbstractMostlySingleton AbstractMostlySingleton.State AbstractNodeVisitor AbstractQualifierPolymorphism AbstractTypeProcessor AbstractValue Acceleration AccumulationAnnotatedTypeFactory AccumulationChecker AccumulationChecker.AliasAnalysis AccumulationTransfer AccumulationVisitor AddAnnotatedFor AFConstraint AFReducer AggregateChecker AliasingAnnotatedTypeFactory AliasingChecker AliasingTransfer AliasingVisitor AlwaysSafe Analysis Analysis.BeforeOrAfter Analysis.Direction AnalysisResult Angle AnnotatedFor AnnotatedTypeCombiner AnnotatedTypeCopier AnnotatedTypeCopierWithReplacement AnnotatedTypeCopierWithReplacement.Visitor AnnotatedTypeFactory AnnotatedTypeFactory.ParameterizedExecutableType AnnotatedTypeFormatter AnnotatedTypeMirror AnnotatedTypeMirror.AnnotatedArrayType AnnotatedTypeMirror.AnnotatedDeclaredType AnnotatedTypeMirror.AnnotatedExecutableType AnnotatedTypeMirror.AnnotatedIntersectionType AnnotatedTypeMirror.AnnotatedNoType AnnotatedTypeMirror.AnnotatedNullType AnnotatedTypeMirror.AnnotatedPrimitiveType AnnotatedTypeMirror.AnnotatedTypeVariable AnnotatedTypeMirror.AnnotatedUnionType AnnotatedTypeMirror.AnnotatedWildcardType AnnotatedTypeParameterBounds AnnotatedTypeReplacer AnnotatedTypes AnnotatedTypeScanner AnnotatedTypeScanner.Reduce AnnotatedTypeVisitor AnnotationBuilder AnnotationClassLoader AnnotationConverter AnnotationEqualityVisitor AnnotationFileElementTypes AnnotationFileParser AnnotationFileParser.AnnotationFileAnnotations AnnotationFileParser.AnnotationFileParserException AnnotationFileParser.RecordComponentStub AnnotationFileParser.RecordStub AnnotationFileResource AnnotationFileStore AnnotationFileUtil AnnotationFileUtil.AnnotationFileType AnnotationFormatter AnnotationMirrorMap AnnotationMirrorSet AnnotationMirrorToAnnotationExprConversion AnnotationProvider AnnotationStatistics AnnotationTransferVisitor AnnotationUtils Area ArrayAccess ArrayAccessNode ArrayCreation ArrayCreationNode ArrayLen ArrayLenRange ArrayTypeNode ArrayWithoutPackage ASceneWrapper AssertionErrorNode AssertNonNullIfNonNull AssignmentNode AsSuperVisitor AtmCombo AtmComboVisitor AutoValueSupport AwtAlphaCompositingRule AwtColorSpace AwtCursorType AwtFlowLayout BackwardAnalysis BackwardAnalysisImpl BackwardTransferFunction BaseAnnotatedTypeFactory BaseAnnotatedTypeFactoryForIndexChecker BaseTypeChecker BaseTypeValidator BaseTypeVisitor BasicAnnotationProvider BasicTypeProcessor BinaryName BinaryNameOrPrimitiveType BinaryNameWithoutPackage BinaryOperation BinaryOperationNode BitwiseAndNode BitwiseComplementNode BitwiseOrNode BitwiseXorNode Block Block.BlockType BlockImpl BooleanLiteralNode BoolVal Bottom BottomThis BottomVal BoundsInitializer BugInCF BuilderFrameworkSupport BuilderFrameworkSupportUtils ByteMath C CalledMethods CalledMethods CalledMethodsAnalysis CalledMethodsAnnotatedTypeFactory CalledMethodsBottom CalledMethodsChecker CalledMethodsPredicate CalledMethodsTransfer CalledMethodsVisitor CanonicalName CanonicalNameAndBinaryName CanonicalNameOrEmpty CanonicalNameOrPrimitiveType CaseNode cd CFAbstractAnalysis CFAbstractAnalysis.FieldInitialValue CFAbstractStore CFAbstractTransfer CFAbstractValue CFAnalysis CFCFGBuilder CFCFGBuilder.CFCFGTranslationPhaseOne CFComment CFGBuilder CFGProcessor CFGProcessor.CFGProcessResult CFGTranslationPhaseOne CFGTranslationPhaseThree CFGTranslationPhaseThree.PredecessorHolder CFGTranslationPhaseTwo CFGVisualizeLauncher CFGVisualizer CFStore CFTransfer CFTreeBuilder CFValue CharacterLiteralNode CheckerMain ClassBound ClassDeclarationNode ClassGetName ClassGetSimpleName ClassName ClassNameNode ClassTypeParamApplier ClassVal ClassValAnnotatedTypeFactory ClassValBottom ClassValChecker ClassValVisitor CollectionToArrayHeuristics CollectionUtils CompareToMethod CompilerMessageKey CompilerMessageKeyBottom CompilerMessagesAnnotatedTypeFactory CompilerMessagesChecker ConditionalAndNode ConditionalBlock ConditionalBlockImpl ConditionalJump ConditionalNotNode ConditionalOrNode ConditionalPostconditionAnnotation ConditionalTransferResult Constant Constant.Type ConstantPropagationPlayground ConstantPropagationStore ConstantPropagationTransfer ConstraintMap ConstraintMapBuilder Contract Contract.ConditionalPostcondition Contract.Kind Contract.Postcondition Contract.Precondition ContractsFromMethod ControlFlowGraph ConversionCategory Covariant CreatesMustCallFor CreatesMustCallFor.List CreatesMustCallForElementSupplier CreatesMustCallForToJavaExpression Current DebugListTreeAnnotator DeclarationsIntoElements Default DefaultAnnotatedTypeFormatter DefaultAnnotatedTypeFormatter.FormattingVisitor DefaultAnnotationFormatter DefaultFor DefaultForTypeAnnotator DefaultInferredTypesApplier DefaultJointVisitor DefaultQualifier DefaultQualifier.List DefaultQualifierForUse DefaultQualifierForUseTypeAnnotator DefaultQualifierInHierarchy DefaultQualifierKindHierarchy DefaultQualifierKindHierarchy.DefaultQualifierKind DefaultQualifierPolymorphism DefaultReflectionResolver DefaultTypeArgumentInference DefaultTypeHierarchy degrees DependentTypesError DependentTypesHelper DependentTypesTreeAnnotator DetachedVarSymbol Deterministic DiagMessage DoNothingChecker DOTCFGVisualizer DotSeparatedIdentifiers DotSeparatedIdentifiersOrPrimitiveType DoubleAnnotatedTypeScanner DoubleJavaParserVisitor DoubleLiteralNode DoubleMath DoubleVal Effect Effect.EffectRange ElementAnnotationApplier ElementAnnotationUtil ElementAnnotationUtil.ErrorTypeKindException ElementAnnotationUtil.UnexpectedAnnotationLocationException ElementQualifierHierarchy ElementUtils EmptyProcessor EnsuresCalledMethods EnsuresCalledMethodsIf EnsuresCalledMethodsIf.List EnsuresCalledMethodsVarArgs EnsuresInitializedFields EnsuresInitializedFields.List EnsuresKeyFor EnsuresKeyFor.List EnsuresKeyForIf EnsuresKeyForIf.List EnsuresLockHeld EnsuresLockHeld.List EnsuresLockHeldIf EnsuresLockHeldIf.List EnsuresLTLengthOf EnsuresLTLengthOf.List EnsuresLTLengthOfIf EnsuresLTLengthOfIf.List EnsuresMinLenIf EnsuresMinLenIf.List EnsuresNonNull EnsuresNonNull.List EnsuresNonNullIf EnsuresNonNullIf.List EnsuresQualifier EnsuresQualifier.List EnsuresQualifierIf EnsuresQualifierIf.List EnumVal EqualitiesSolver EqualityAtmComparer EqualsMethod EqualToNode EquivalentAtmComboScanner ExceptionBlock ExceptionBlockImpl ExecUtil ExecUtil.Redirection ExpectedTreesVisitor ExplicitThisNode ExtendedNode ExtendedNode.ExtendedNodeType F2A F2AReducer FBCBottom Fenum FenumAnnotatedTypeFactory FenumBottom FenumChecker FenumTop FenumUnqualified FenumVisitor FieldAccess FieldAccessNode FieldDescriptor FieldDescriptorForPrimitive FieldDescriptorWithoutPackage FieldInvariant FieldInvariants FileAnnotationFileResource FindDistinct FIsA FIsAReducer FloatingDivisionNode FloatingRemainderNode FloatLiteralNode FloatMath FluentAPIGenerator Force FormalParameter Format FormatBottom FormatMethod FormatMethod FormatterAnnotatedTypeFactory FormatterChecker FormatterTransfer FormatterTreeUtil FormatterTreeUtil.InvocationType FormatterTreeUtil.Result FormatterVisitor FormatUtil FormatUtil.ExcessiveOrMissingFormatArgumentException FormatUtil.IllegalFormatConversionCategoryException ForName ForwardAnalysis ForwardAnalysisImpl ForwardTransferFunction FqBinaryName FromByteCode FromStubFile FullyQualifiedName FunctionalInterfaceNode g GenericAnnotatedTypeFactory GenericAnnotatedTypeFactory.ScanState GetClass GetConstructor GetMethod GlbUtil GreaterThanNode GreaterThanOrEqualNode GTENegativeOne GuardedBy GuardedByBottom GuardedByUnknown GuardSatisfied GuiEffectChecker GuiEffectTypeFactory GuiEffectVisitor h HashcodeAtmVisitor HasQualifierParameter HasSubsequence Heuristics Heuristics.Matcher Heuristics.OfKind Heuristics.OrMatcher Heuristics.PreceededBy Heuristics.Within Heuristics.WithinTrueBranch Holding I18nAnnotatedTypeFactory I18nChecker I18nChecksFormat I18nConversionCategory I18nFormat I18nFormatBottom I18nFormatFor I18nFormatterAnnotatedTypeFactory I18nFormatterChecker I18nFormatterTransfer I18nFormatterTreeUtil I18nFormatterTreeUtil.FormatType I18nFormatterVisitor I18nFormatUtil I18nInvalidFormat I18nMakeFormat I18nSubchecker I18nUnknownFormat I18nValidFormat Identifier IdentifierOrPrimitiveType IdentityMostlySingleton IgnoreInWholeProgramInference ImplicitThisNode IndexAbstractTransfer IndexChecker IndexFor IndexMethodIdentifier IndexOrHigh IndexOrLow IndexRefinementInfo IndexUtil InferenceResult InferredValue InferredValue.InferredTarget InferredValue.InferredType InheritableMustCall InheritedAnnotation InitializationAnnotatedTypeFactory InitializationChecker InitializationStore InitializationTransfer InitializationVisitor Initialized InitializedFields InitializedFieldsAnnotatedTypeFactory InitializedFieldsBottom InitializedFieldsChecker InitializedFieldsTransfer InsertAjavaAnnotations InstanceOfNode IntegerDivisionNode IntegerLiteralNode IntegerMath IntegerRemainderNode InternalForm InternalUtils Interned InternedDistinct InterningAnnotatedTypeFactory InterningChecker InterningVisitor InternMethod IntRange IntRangeFromGTENegativeOne IntRangeFromNonNegative IntRangeFromPositive IntVal InvalidFormat InvisibleQualifier Invoke IrrelevantTypeAnnotator JarEntryAnnotationFileResource JavaCodeStatistics JavaExpression JavaExpression JavaExpressionConverter JavaExpressionOptimizer JavaExpressionParseUtil JavaExpressionParseUtil.JavaExpressionParseException JavaExpressionScanner JavaExpressionVisitor JavaParserUtil JavaParserUtil.StringLiteralConcatenateVisitor JavaStubifier JointJavacJavaParserVisitor JointVisitorWithDefaultAction K KeyFor KeyForAnalysis KeyForAnnotatedTypeFactory KeyForAnnotatedTypeFactory.KeyForTypeHierarchy KeyForBottom KeyForPropagationTreeAnnotator KeyForPropagator KeyForPropagator.PropagationDirection KeyForStore KeyForSubchecker KeyForTransfer KeyForValue kg km km2 km3 kmPERh kN Label LambdaResultExpressionNode LeakedToResult LeftShiftNode Length LengthOf LessThan LessThanAnnotatedTypeFactory LessThanBottom LessThanChecker LessThanNode LessThanOrEqualNode LessThanTransfer LessThanUnknown LessThanVisitor ListTreeAnnotator ListTypeAnnotator LiteralKind LiteralTreeAnnotator LiveVariablePlayground LiveVarStore LiveVarTransfer LiveVarValue LocalizableKey LocalizableKeyAnnotatedTypeFactory LocalizableKeyBottom LocalizableKeyChecker Localized LocalVariable LocalVariableNode LockAnalysis LockAnnotatedTypeFactory LockChecker LockHeld LockingFree LockPossiblyHeld LockStore LockTransfer LockTreeAnnotator LockVisitor LombokSupport LongLiteralNode LongMath LowerBoundAnnotatedTypeFactory LowerBoundBottom LowerBoundChecker LowerBoundTransfer LowerBoundUnknown LowerBoundVisitor LTEqLengthOf LTLengthOf LTOMLengthOf Luminance m m2 m3 MarkerNode Mass MatchesRegex MaybeAliased MaybeLeaked MaybePresent MayReleaseLocks MethodAccessNode MethodApplier MethodCall MethodDescriptor MethodInvocationNode MethodTypeParamApplier MethodVal MethodValAnnotatedTypeFactory MethodValBottom MethodValChecker MethodValVisitor min MinLen MinLenFieldInvariant MixedUnits mm mm2 mm3 mol MonotonicNonNull MonotonicQualifier MostlyNoElementQualifierHierarchy MostlySingleton mPERs mPERs2 MustCall MustCallAlias MustCallAnnotatedTypeFactory MustCallChecker MustCallInferenceLogic MustCallNoCreatesMustCallForChecker MustCallTransfer MustCallTypeAnnotator MustCallUnknown MustCallVisitor N NarrowingConversionNode NegativeIndexFor NewInstance NewObject Node NoDefaultQualifierForUse NodeUtils NodeVisitor NoElementQualifierHierarchy NonLeaked NonNegative NonNull NoQualifierParameter NotCalledMethods NotEqualNode NotOnlyInitialized NotOwning Nullable NullChkNode NullLiteralNode NullnessAnalysis NullnessAnnotatedTypeFactory NullnessAnnotatedTypeFactory.NullnessPropagationTreeAnnotator NullnessAnnotatedTypeFormatter NullnessAnnotatedTypeFormatter.NullnessFormattingVisitor NullnessChecker NullnessStore NullnessTransfer NullnessUtil NullnessValue NullnessVisitor NumberMath NumberUtils NumericalAdditionNode NumericalMinusNode NumericalMultiplicationNode NumericalPlusNode NumericalSubtractionNode ObjectCreationNode OffsetDependentTypesHelper OffsetEquation Opt OptionalBottom OptionalChecker OptionalVisitor OptionConfiguration Owning PackageNameNode Pair ParamApplier ParameterizedTypeNode PartialRegex PhaseOneResult PolyFenum PolyIndex PolyInitializedFields PolyInterned PolyKeyFor PolyLength PolyLowerBound PolymorphicQualifier PolyMustCall PolyNull PolyPresent PolyRegex PolySameLen PolySignature PolySigned PolyTainted PolyUI PolyUIEffect PolyUIType PolyUnit PolyUpperBound PolyValue Positive PostconditionAnnotation PreconditionAnnotation Prefix Present PrimitiveType PrimitiveTypeNode PropagationTreeAnnotator PropagationTypeAnnotator PropertyKey PropertyKeyAnnotatedTypeFactory PropertyKeyBottom PropertyKeyChecker Pure Pure Pure.Kind PurityAnnotatedTypeFactory PurityChecker PurityChecker PurityChecker.PurityCheckerHelper PurityChecker.PurityResult PurityUnqualified PurityUtils QualifierArgument QualifierDefaults QualifierDefaults.BoundType QualifierForLiterals QualifierHierarchy QualifierKind QualifierKindHierarchy QualifierPolymorphism QualifierUpperBounds radians Range ReflectionResolver ReflectiveEvaluator Regex RegexAnnotatedTypeFactory RegexBottom RegexChecker RegexTransfer RegexUtil RegexUtil.CheckedPatternSyntaxException RegexVisitor RegularBlock RegularBlockImpl RegularTransferResult ReleasesNoLocks RelevantJavaTypes RemoveAnnotationsForInference ReportCall ReportChecker ReportCreation ReportInherit ReportOverride ReportReadWrite ReportUnqualified ReportUse ReportVisitor ReportWrite RequiresCalledMethods RequiresCalledMethods.List RequiresNonNull RequiresNonNull.List RequiresQualifier RequiresQualifier.List Resolver ResourceLeakAnalysis ResourceLeakAnnotatedTypeFactory ResourceLeakChecker ResourceLeakTransfer ResourceLeakVisitor ReturnNode ReturnsFormat ReturnsReceiver ReturnsReceiverAnnotatedTypeFactory ReturnsReceiverChecker ReturnsReceiverVisitor s SafeEffect SafeType SameLen SameLenAnnotatedTypeFactory SameLenBottom SameLenChecker SameLenTransfer SameLenUnknown SameLenVisitor SceneToStubWriter SearchIndexAnnotatedTypeFactory SearchIndexBottom SearchIndexChecker SearchIndexFor SearchIndexTransfer SearchIndexUnknown ShortLiteralNode ShortMath SideEffectFree SignatureAnnotatedTypeFactory SignatureBottom SignatureChecker SignaturePrinter SignatureTransfer SignatureUnknown Signed SignednessAnnotatedTypeFactory SignednessBottom SignednessChecker SignednessGlb SignednessUtil SignednessUtilExtra SignednessVisitor SignedPositive SignedPositiveFromUnsigned SignedRightShiftNode SimpleAnnotatedTypeScanner SimpleAnnotatedTypeScanner.DefaultAction SimpleAnnotatedTypeVisitor SingleSuccessorBlock SingleSuccessorBlockImpl SourceChecker SourceVisitor SpecialBlock SpecialBlock.SpecialBlockType SpecialBlockImpl Speed StaticallyExecutable Store Store.FlowRule Store.Kind StringCFGVisualizer StringConcatenateAssignmentNode StringConcatenateNode StringConversionNode StringLiteralNode StringToJavaExpression StringVal StructuralEqualityComparer StructuralEqualityVisitHistory StubFiles StubGenerator Subsequence Substance SubstringIndexAnnotatedTypeFactory SubstringIndexBottom SubstringIndexChecker SubstringIndexFor SubstringIndexUnknown SubtypeIsSubsetQualifierHierarchy SubtypeIsSupersetQualifierHierarchy SubtypeOf SubtypesSolver SubtypeVisitHistory SubtypingAnnotatedTypeFactory SubtypingAnnotationClassLoader SubtypingChecker SuperNode SuperTypeApplier SupertypesSolver SupportedLintOptions SupportedOptions SuppressWarningsPrefix SwingBoxOrientation SwingCompassDirection SwingElementOrientation SwingHorizontalOrientation SwingSplitPaneOrientation SwingTextOrientation SwingTitleJustification SwingTitlePosition SwingVerticalOrientation SwitchExpressionNode SwitchExpressionScanner SwitchExpressionScanner.FunctionalSwitchExpressionScanner SynchronizedNode SyntheticArrays SystemGetPropertyHandler SystemUtil t Tainted TaintingChecker TaintingVisitor TargetConstraints TargetConstraints.Equalities TargetConstraints.Subtypes TargetConstraints.Supertypes TargetLocations Temperature TerminatesExecution TernaryExpressionNode This ThisNode ThisReference ThrowNode Time TIsU ToIndexFileConverter TransferFunction TransferInput TransferResult TreeAnnotator TreeBuilder TreeDebug TreeDebug.Visitor TreeParser TreePathCacher TreePathUtil TreePrinter TreeScannerWithDefaults TreeUtils TSubU TSuperU TUConstraint TypeAnnotationMover TypeAnnotationUtils TypeAnnotator TypeArgInferenceUtil TypeArgumentInference TypeArgumentMapper TypeCastNode TypeDeclarationApplier TypeHierarchy TypeKind TypeKindUtils TypeKindUtils.PrimitiveConversionKind TypeOutputtingChecker TypeOutputtingChecker.GeneralAnnotatedTypeFactory TypeOutputtingChecker.Visitor TypesIntoElements TypesUtils TypeSystemError TypeUseLocation TypeValidator TypeVariableSubstitutor TypeVarUseApplier TypeVisualizer UBQualifier UBQualifier.LessThanLengthOf UBQualifier.UpperBoundLiteralQualifier UBQualifier.UpperBoundUnknownQualifier UI UIEffect UIPackage UIType UnaryOperation UnaryOperationNode UnderInitialization UnderlyingAST UnderlyingAST.CFGLambda UnderlyingAST.CFGMethod UnderlyingAST.CFGStatement UnderlyingAST.Kind Unique UnitsAnnotatedTypeFactory UnitsAnnotatedTypeFactory.UnitsQualifierKindHierarchy UnitsAnnotatedTypeFormatter UnitsAnnotatedTypeFormatter.UnitsAnnotationFormatter UnitsAnnotatedTypeFormatter.UnitsFormattingVisitor UnitsAnnotationClassLoader UnitsBottom UnitsChecker UnitsMultiple UnitsRelations UnitsRelations UnitsRelationsDefault UnitsRelationsTools UnitsTools UnitsVisitor Unknown UnknownClass UnknownCompilerMessageKey UnknownFormat UnknownInitialization UnknownInterned UnknownKeyFor UnknownLocalizableKey UnknownLocalized UnknownMethod UnknownPropertyKey UnknownRegex UnknownSignedness UnknownThis UnknownUnits UnknownVal UnmodifiableIdentityHashMap Unqualified Unsigned UnsignedRightShiftNode Untainted Unused UpperBoundAnnotatedTypeFactory UpperBoundBottom UpperBoundChecker UpperBoundFor UpperBoundLiteral UpperBoundTransfer UpperBoundUnknown UpperBoundVisitor UserError UsesObjectEquals ValueAnnotatedTypeFactory ValueChecker ValueCheckerUtils ValueLiteral ValueLiteralNode ValueTransfer ValueVisitor VariableApplier VariableDeclarationNode ViewpointAdaptJavaExpression VoidVisitorWithDefaultAction Volume WholeProgramInference WholeProgramInference.OutputFormat WholeProgramInferenceImplementation WholeProgramInferenceJavaParserStorage WholeProgramInferenceScenesStorage WholeProgramInferenceScenesStorage.AnnotationsInContexts WholeProgramInferenceStorage WideningConversionNode";

  /**
   * For example, if we have @Annotation] coming from the diff algorithm, we will extract
   * the @Annotation part.
   *
   * <p>
   *
   * @param anno an annotation with length greater than 2, might contain "]" at the end or not.
   * @return the annotation itself without no "]"
   */
  private static String TrimAnnotation(@MinLen(2) String anno) {
    @SuppressWarnings(
        "index:assignment") // This method should only be called on strings that contain an "@"
    @NonNegative int index1 = anno.indexOf('@');
    String result = "";
    /* if apart from the '@' symbol, the anno contains only alphabetical elements (for example: @NulLable), we will take
    the whole string. Otherwise, for cases such as @Nullable], we will ignore the last element of the anno.
    */
    int indexConsider=anno.indexOf("]");
    if (indexConsider==-1) {
      result = anno.substring(index1, anno.length());
    } else {
      result = anno.substring(index1, indexConsider);
    }
    return result;
  }

  /**
   * This method will take a line, which begins with an annotation, and return the first annotation
   * in that line
   *
   * <p>
   *
   * @param line a non-empty line beginning with an annotation a
   * @return the annotation which the line begins with
   */
  private static String getAnnos(String line) {
    String[] words = line.split(" ");
    String result = words[0];
    return "@" + result;
  }

  /**
   * This method is to count the number of annotations in a line
   *
   * <p>
   *
   * @param line a line
   * @return the number of annotations in that line
   */
  private static int countAnnos(String line) {
    int count = 0;
    boolean checkinString = true;
    for (int i = 0; i < line.length(); i++) {
      if (line.charAt(i) == '\"') {
        if (checkinString) {
          checkinString = false;
        } else {
          checkinString = true;
        }
      }
      if (line.charAt(i) == '@' && checkinString) {
        count++;
      }
    }
    return count;
  }

  /**
   * This method is to check if a particular index in a line belong to a string literal or not
   *
   * <p>
   *
   * @param line a line
   * @param Index index of line
   * @return true if Index belong to a string literal, false otherwise
   */
  private static boolean checkInString(@IndexFor("#2") int Index, String line) {
    int before = 0;
    int after = 0;
    for (int i = 0; i < Index; i++) {
      if (line.charAt(i) == '\"') {
        before++;
      }
    }
    for (int i = Index + 1; i < line.length(); i++) {
      if (line.charAt(i) == '\"') {
        after++;
      }
    }
    if (before % 2 == 0 && after % 2 == 0) {
      return true;
    }
    return false;
  }

  /**
   * This method is to trim out all the comments in a human-written or computer-generated line
   * before passing the line to the Diff algorithm.
   *
   * <p>
   *
   * @param line a line with or without a comment section
   * @return that line without any comment
   */
  private static String ignoreComment(String line) {
    int indexComment = line.length() + 1;
    String finalLine = line;
    int indexDash = line.indexOf("//");
    int indexStar = line.indexOf("*");
    int indexDashStar = line.indexOf("/*");
    if (indexDash != -1) {
      if (indexDash == 0) {
        finalLine = line.substring(0, indexDash);
      } else {
        finalLine = line.substring(0, indexDash - 1);
      }
    }
    if (indexDashStar != -1) {
      finalLine = line.substring(0, indexDashStar);
    }
    if (indexStar == 0) {
      finalLine = line.substring(0, 0);
    }
    return finalLine;
  }

  /**
   * This method is to format a line in a computer-generated file before passing it to the Diff
   * algorithm. The way this method format the line is to change all annotaions written in the
   * "@org.checkerframework..." format to the human -written format.
   *
   * <p>
   *
   * @param line a line belong to a computer-generated file
   * @return the same line with all the annotations being changed to the human-written format.
   */
  private static String extractCheckerPackage(String line) {
    String[] temp = line.split(" ");
    String result = "";
    if (line.length() == 0) {
      result = "";
    } else {
      for (String word : temp) {
        if (word.contains("@org.checkerframework")) {
          String[] tempo = word.split("[.]");
          String tempResult = "@" + tempo[tempo.length - 1];
          if (word == temp[1]) {
            result = " " + tempResult;
          } else {
            result = result + " " + tempResult;
          }
        } else {
          if (word == temp[0]) {
            result = word;
          } else {
            result = result + " " + word;
          }
        }
      }
    }
    return result;
  }

  /**
   * This method is to trim out the paranthese part in an algorithm, for example, @Annotation(abc)
   * will be changed to @Annotation.
   *
   * <p>This method need to be used with care. We want to use it to update the final result. This
   * method should not be used for any list or string that will become the input of the Diff
   * algorithm. If we do that, the Diff algorithm will not be able to recognize any potential
   * difference in the parantheses between an annotation written by human and an annotation generated by
   * the computer anymore.
   *
   * <p>
   *
   * @param anno the annotation which will be trimmed
   * @return that annotation without the paranthese part
   */
  private static String trimPathen(String anno) {
    int para = anno.indexOf("(");
    if (para == -1) {
      return anno;
    }
    return anno.substring(0, para);
  }

   /**
   * This method is to print out all the annotations belong to a line.
   *
   * <p>
   *
   * @param str   a line
   * @return      a Linked List containing all annotations of str.
   */
  private static LinkedList<String> extractString(String str) {
    LinkedList<String> result = new LinkedList<String>();
    String anno = "";
    int countAnno = countAnnos(str);
    String temp = str;
    for (int i = 0; i < countAnno; i++) {
      int index1 = temp.indexOf("@");
      temp = temp.substring(index1 + 1, temp.length());
      anno = getAnnos(temp);
      String tempAnno = anno.substring(1, anno.length());
      if (checkInString(index1, str) && checkerFramworkPackage.contains(tempAnno)) {
        if (anno.contains("(")) {
          if (str.contains(")")) {
            anno = str.substring(index1, str.indexOf(")") + 1);
          } else {
            anno = str.substring(index1, str.length());
          }
        }
        result.add(anno);
      }
    }
    return result;
  }
  /**
   * The main entry point. Running this outputs the percentage of annotations in some source file
   * that were inferred by WPI.
   *
   * <p>-param args the files. The first element is the original source file. All remaining elements
   * should be corresponding .ajava files produced by WPI. This program assumes that all inputs have
   * been converted to some unified formatting style to eliminate unnecessary changes (e.g., by
   * running gjf on each input).
   */
  public static void main(String[] args) {
    if (args.length <= 1) {
      throw new RuntimeException(
          "Provide at least one .java file and one or more" + ".ajava files.");
    }
    List<String> originalFile = new ArrayList<String>();
    // specific annotations and the number of computer-written files missing them
    Map<String, Integer> AnnoLocate = new HashMap<String, Integer>();
    // the name of the types of annotation and their numbers in the human-written file
    Map<String, Integer> AnnoCount = new HashMap<String, Integer>();
    /* the name of the types of annotations and their "correct" numbers (meaning the number of annotations of that
    type not missed by computer-written files) */
    Map<String, Integer> AnnoSimilar = new HashMap<String, Integer>();
    File file = new File(args[0]);
    try (FileReader fr = new FileReader(file)) {
      BufferedReader br = new BufferedReader(fr);
      @NonNull String str;
      int pos = -1;
      LinkedList<String> annoList = new LinkedList<String>();
      while ((str = br.readLine()) != null) {
        pos++;
        str = ignoreComment(str);
        originalFile.add(str);
        annoList = extractString(str);
        for (String anno : annoList) {
          String annoNoPara = trimPathen(anno);
          if (AnnoCount.containsKey(annoNoPara)) {
            int numberOfAnno = AnnoCount.get(annoNoPara);
            AnnoCount.put(annoNoPara, numberOfAnno + 1);
          } else {
            AnnoCount.put(annoNoPara, new Integer(1));
          }
          AnnoSimilar.put(annoNoPara, new Integer(0));
          // we want the keys in the map AnnoLocate has this following format: type_position
          String posi = String.valueOf(pos);
          AnnoLocate.put(anno + "_" + posi, new Integer(0));
        }
      }
      fr.close();
    } catch (Exception e) {
      throw new RuntimeException("Could not read file: " + args[0] + ". Check that it exists?");
    }
    List<Patch<String>> diffs = new ArrayList<>(args.length - 1);
    for (int i = 1; i < args.length; ++i) {
      file = new File(args[i]);
      try (FileReader fr = new FileReader(file)) {
        List<String> newFile = new ArrayList<String>();
        BufferedReader br = new BufferedReader(fr);
        String str;
        LinkedList<String> annoList = new LinkedList<String>();
        while ((str = br.readLine()) != null) {
          str = ignoreComment(str);
          str = extractCheckerPackage(str);
          newFile.add(str);
        }
        diffs.add(DiffUtils.diff(originalFile, newFile));
      } catch (Exception e) {
        throw new RuntimeException("Could not read file: " + args[i] + ". Check that it exists?");
      }
    }
    for (int i = 0; i < args.length - 1; i++) {
      Patch<String> patch = diffs.get(i);
      boolean notcomment = false;
      for (AbstractDelta<String> delta : patch.getDeltas()) {
        // get the delta in string format
        String deltaInString = delta.toString();
        notcomment = true;
        String newpos = "";
        // we change the delta output to a string, then break that string into different parts
        List<String> myList = new ArrayList<String>(Arrays.asList(deltaInString.split(" ")));
        // just take the delta with annotations into consideration
        if (deltaInString.contains("@")) {
          // get the position of that annotation in the delta, which is something like "5," or "6,".
          String pos = myList.get(2);
          // take the "," out
          if (pos.length() > 1) {
            newpos = pos.substring(0, pos.length() - 1);
          }
          String result = "";
          for (String element : myList) {
            // we dont take differences in the comment section into consideration
            if (element.equals("[")) {
              notcomment = true;
            }
            if (element.equals("//")) {
              notcomment = false;
            }
            if (notcomment && element.contains("@")) {
              if (element.length() > 2) {
                element = TrimAnnotation(element);
                int currLine = Integer.parseInt(newpos);
                // to match the one in AnnoLocate
                result = element + "_" + newpos;
                // update the data of AnnoLocate
                if (AnnoLocate.containsKey(result)) {
                  int value = AnnoLocate.get(result);
                  AnnoLocate.put(result, value + 1);
                } else {
                  while (currLine < AnnoLocate.size()) {
                    currLine++;
                    result = element + currLine;
                    if (AnnoLocate.containsKey(result)) {
                      int value = AnnoLocate.get(result);
                      AnnoLocate.put(result, value + 1);
                      break;
                    }
                  }
                }
              }
            }
          }
        }
      }
    }
    // update the data of AnnoSimilar
    for (Map.Entry<String, Integer> me : AnnoLocate.entrySet()) {
      String annoName = me.getKey();
      /*if the number of computer-written code missing that element is less than the total number of codes written
      by computer, the at least one of those computer-written code must have gotten the annotation correct*/
      if (me.getValue() < args.length - 1) {
        // for example, if we have @Option_345, we will only need "@Option" since we want the
        // general type here
        int index = annoName.indexOf("_");
        if (index >= 0) annoName = annoName.substring(0, index);
        annoName = trimPathen(annoName);
        int value = AnnoSimilar.get(annoName);
        value = value + 1;
        AnnoSimilar.put(annoName, value);
      }
    }
    System.out.println();
    for (Map.Entry<String, Integer> e : AnnoCount.entrySet()) {
      int totalCount = e.getValue();
      String value = e.getKey();
      int correctCount = AnnoSimilar.get(value);
      System.out.println(value + " got " + correctCount + "/" + totalCount);
    }
    System.out.println();
  }
}
