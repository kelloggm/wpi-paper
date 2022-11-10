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
import org.checkerframework.checker.index.qual.NonNegative;
import org.checkerframework.common.value.qual.MinLen;

/**
 * The entry point for the inferred annos counter. Run this by passing arguments, with the first
 * being the path of the human-written file, and other arguments being the path of the .ajava files
 * produced by the WPI. The path here should be relative to "experiments\inferred-annos-counter."
 * The program assumes that a formatter has been applied, so it is important to run the script
 * format.sh in the directory "experiments" beforehand. InferredAnnosCounter only takes one
 * human-written source file at a time. So in case it is needed to run multiple human-written files
 * with corresponding computer-generated files, InferredAnnosCounter needs to be invoked multiple
 * times. The way to run InferredAnnosCounter is like this: cd experiments\inferred-annos-counter
 * (going to the working directory) and then gradle run --args = "(a path to the human-written file)
 * (one or more paths to the computer-generated files)". The result will not be in alphabetical
 * order.
 */
public class InferredAnnosCounter {
  private static String checkerFramworkPackage =
      "A2F A2FReducer AbstractAnalysis AbstractAnalysis.Worklist AbstractAtmComboVisitor AbstractCFGVisualizer AbstractCFGVisualizer.VisualizeWhere AbstractMostlySingleton AbstractMostlySingleton.State AbstractNodeVisitor AbstractQualifierPolymorphism AbstractTypeProcessor AbstractValue Acceleration AccumulationAnnotatedTypeFactory AccumulationChecker AccumulationChecker.AliasAnalysis AccumulationTransfer AccumulationVisitor AddAnnotatedFor AFConstraint AFReducer AggregateChecker AliasingAnnotatedTypeFactory AliasingChecker AliasingTransfer AliasingVisitor AlwaysSafe Analysis Analysis.BeforeOrAfter Analysis.Direction AnalysisResult Angle AnnotatedFor AnnotatedTypeCombiner AnnotatedTypeCopier AnnotatedTypeCopierWithReplacement AnnotatedTypeCopierWithReplacement.Visitor AnnotatedTypeFactory AnnotatedTypeFactory.ParameterizedExecutableType AnnotatedTypeFormatter AnnotatedTypeMirror AnnotatedTypeMirror.AnnotatedArrayType AnnotatedTypeMirror.AnnotatedDeclaredType AnnotatedTypeMirror.AnnotatedExecutableType AnnotatedTypeMirror.AnnotatedIntersectionType AnnotatedTypeMirror.AnnotatedNoType AnnotatedTypeMirror.AnnotatedNullType AnnotatedTypeMirror.AnnotatedPrimitiveType AnnotatedTypeMirror.AnnotatedTypeVariable AnnotatedTypeMirror.AnnotatedUnionType AnnotatedTypeMirror.AnnotatedWildcardType AnnotatedTypeParameterBounds AnnotatedTypeReplacer AnnotatedTypes AnnotatedTypeScanner AnnotatedTypeScanner.Reduce AnnotatedTypeVisitor AnnotationBuilder AnnotationClassLoader AnnotationConverter AnnotationEqualityVisitor AnnotationFileElementTypes AnnotationFileParser AnnotationFileParser.AnnotationFileAnnotations AnnotationFileParser.AnnotationFileParserException AnnotationFileParser.RecordComponentStub AnnotationFileParser.RecordStub AnnotationFileResource AnnotationFileStore AnnotationFileUtil AnnotationFileUtil.AnnotationFileType AnnotationFormatter AnnotationMirrorMap AnnotationMirrorSet AnnotationMirrorToAnnotationExprConversion AnnotationProvider AnnotationStatistics AnnotationTransferVisitor AnnotationUtils Area ArrayAccess ArrayAccessNode ArrayCreation ArrayCreationNode ArrayLen ArrayLenRange ArrayTypeNode ArrayWithoutPackage ASceneWrapper AssertionErrorNode AssertNonNullIfNonNull AssignmentNode AsSuperVisitor AtmCombo AtmComboVisitor AutoValueSupport AwtAlphaCompositingRule AwtColorSpace AwtCursorType AwtFlowLayout BackwardAnalysis BackwardAnalysisImpl BackwardTransferFunction BaseAnnotatedTypeFactory BaseAnnotatedTypeFactoryForIndexChecker BaseTypeChecker BaseTypeValidator BaseTypeVisitor BasicAnnotationProvider BasicTypeProcessor BinaryName BinaryNameOrPrimitiveType BinaryNameWithoutPackage BinaryOperation BinaryOperationNode BitwiseAndNode BitwiseComplementNode BitwiseOrNode BitwiseXorNode Block Block.BlockType BlockImpl BooleanLiteralNode BoolVal Bottom BottomThis BottomVal BoundsInitializer BugInCF BuilderFrameworkSupport BuilderFrameworkSupportUtils ByteMath C CalledMethods CalledMethods CalledMethodsAnalysis CalledMethodsAnnotatedTypeFactory CalledMethodsBottom CalledMethodsChecker CalledMethodsPredicate CalledMethodsTransfer CalledMethodsVisitor CanonicalName CanonicalNameAndBinaryName CanonicalNameOrEmpty CanonicalNameOrPrimitiveType CaseNode cd CFAbstractAnalysis CFAbstractAnalysis.FieldInitialValue CFAbstractStore CFAbstractTransfer CFAbstractValue CFAnalysis CFCFGBuilder CFCFGBuilder.CFCFGTranslationPhaseOne CFComment CFGBuilder CFGProcessor CFGProcessor.CFGProcessResult CFGTranslationPhaseOne CFGTranslationPhaseThree CFGTranslationPhaseThree.PredecessorHolder CFGTranslationPhaseTwo CFGVisualizeLauncher CFGVisualizer CFStore CFTransfer CFTreeBuilder CFValue CharacterLiteralNode CheckerMain ClassBound ClassDeclarationNode ClassGetName ClassGetSimpleName ClassName ClassNameNode ClassTypeParamApplier ClassVal ClassValAnnotatedTypeFactory ClassValBottom ClassValChecker ClassValVisitor CollectionToArrayHeuristics CollectionUtils CompareToMethod CompilerMessageKey CompilerMessageKeyBottom CompilerMessagesAnnotatedTypeFactory CompilerMessagesChecker ConditionalAndNode ConditionalBlock ConditionalBlockImpl ConditionalJump ConditionalNotNode ConditionalOrNode ConditionalPostconditionAnnotation ConditionalTransferResult Constant Constant.Type ConstantPropagationPlayground ConstantPropagationStore ConstantPropagationTransfer ConstraintMap ConstraintMapBuilder Contract Contract.ConditionalPostcondition Contract.Kind Contract.Postcondition Contract.Precondition ContractsFromMethod ControlFlowGraph ConversionCategory Covariant CreatesMustCallFor CreatesMustCallFor.List CreatesMustCallForElementSupplier CreatesMustCallForToJavaExpression Current DebugListTreeAnnotator DeclarationsIntoElements Default DefaultAnnotatedTypeFormatter DefaultAnnotatedTypeFormatter.FormattingVisitor DefaultAnnotationFormatter DefaultFor DefaultForTypeAnnotator DefaultInferredTypesApplier DefaultJointVisitor DefaultQualifier DefaultQualifier.List DefaultQualifierForUse DefaultQualifierForUseTypeAnnotator DefaultQualifierInHierarchy DefaultQualifierKindHierarchy DefaultQualifierKindHierarchy.DefaultQualifierKind DefaultQualifierPolymorphism DefaultReflectionResolver DefaultTypeArgumentInference DefaultTypeHierarchy degrees DependentTypesError DependentTypesHelper DependentTypesTreeAnnotator DetachedVarSymbol Deterministic DiagMessage DoNothingChecker DOTCFGVisualizer DotSeparatedIdentifiers DotSeparatedIdentifiersOrPrimitiveType DoubleAnnotatedTypeScanner DoubleJavaParserVisitor DoubleLiteralNode DoubleMath DoubleVal Effect Effect.EffectRange ElementAnnotationApplier ElementAnnotationUtil ElementAnnotationUtil.ErrorTypeKindException ElementAnnotationUtil.UnexpectedAnnotationLocationException ElementQualifierHierarchy ElementUtils EmptyProcessor EnsuresCalledMethods EnsuresCalledMethodsIf EnsuresCalledMethodsIf.List EnsuresCalledMethodsVarArgs EnsuresInitializedFields EnsuresInitializedFields.List EnsuresKeyFor EnsuresKeyFor.List EnsuresKeyForIf EnsuresKeyForIf.List EnsuresLockHeld EnsuresLockHeld.List EnsuresLockHeldIf EnsuresLockHeldIf.List EnsuresLTLengthOf EnsuresLTLengthOf.List EnsuresLTLengthOfIf EnsuresLTLengthOfIf.List EnsuresMinLenIf EnsuresMinLenIf.List EnsuresNonNull EnsuresNonNull.List EnsuresNonNullIf EnsuresNonNullIf.List EnsuresQualifier EnsuresQualifier.List EnsuresQualifierIf EnsuresQualifierIf.List EnumVal EqualitiesSolver EqualityAtmComparer EqualsMethod EqualToNode EquivalentAtmComboScanner ExceptionBlock ExceptionBlockImpl ExecUtil ExecUtil.Redirection ExpectedTreesVisitor ExplicitThisNode ExtendedNode ExtendedNode.ExtendedNodeType F2A F2AReducer FBCBottom Fenum FenumAnnotatedTypeFactory FenumBottom FenumChecker FenumTop FenumUnqualified FenumVisitor FieldAccess FieldAccessNode FieldDescriptor FieldDescriptorForPrimitive FieldDescriptorWithoutPackage FieldInvariant FieldInvariants FileAnnotationFileResource FindDistinct FIsA FIsAReducer FloatingDivisionNode FloatingRemainderNode FloatLiteralNode FloatMath FluentAPIGenerator Force FormalParameter Format FormatBottom FormatMethod FormatMethod FormatterAnnotatedTypeFactory FormatterChecker FormatterTransfer FormatterTreeUtil FormatterTreeUtil.InvocationType FormatterTreeUtil.Result FormatterVisitor FormatUtil FormatUtil.ExcessiveOrMissingFormatArgumentException FormatUtil.IllegalFormatConversionCategoryException ForName ForwardAnalysis ForwardAnalysisImpl ForwardTransferFunction FqBinaryName FromByteCode FromStubFile FullyQualifiedName FunctionalInterfaceNode g GenericAnnotatedTypeFactory GenericAnnotatedTypeFactory.ScanState GetClass GetConstructor GetMethod GlbUtil GreaterThanNode GreaterThanOrEqualNode GTENegativeOne GuardedBy GuardedByBottom GuardedByUnknown GuardSatisfied GuiEffectChecker GuiEffectTypeFactory GuiEffectVisitor h HashcodeAtmVisitor HasQualifierParameter HasSubsequence Heuristics Heuristics.Matcher Heuristics.OfKind Heuristics.OrMatcher Heuristics.PreceededBy Heuristics.Within Heuristics.WithinTrueBranch Holding I18nAnnotatedTypeFactory I18nChecker I18nChecksFormat I18nConversionCategory I18nFormat I18nFormatBottom I18nFormatFor I18nFormatterAnnotatedTypeFactory I18nFormatterChecker I18nFormatterTransfer I18nFormatterTreeUtil I18nFormatterTreeUtil.FormatType I18nFormatterVisitor I18nFormatUtil I18nInvalidFormat I18nMakeFormat I18nSubchecker I18nUnknownFormat I18nValidFormat Identifier IdentifierOrPrimitiveType IdentityMostlySingleton IgnoreInWholeProgramInference ImplicitThisNode IndexAbstractTransfer IndexChecker IndexFor IndexMethodIdentifier IndexOrHigh IndexOrLow IndexRefinementInfo IndexUtil InferenceResult InferredValue InferredValue.InferredTarget InferredValue.InferredType InheritableMustCall InheritedAnnotation InitializationAnnotatedTypeFactory InitializationChecker InitializationStore InitializationTransfer InitializationVisitor Initialized InitializedFields InitializedFieldsAnnotatedTypeFactory InitializedFieldsBottom InitializedFieldsChecker InitializedFieldsTransfer InsertAjavaAnnotations InstanceOfNode IntegerDivisionNode IntegerLiteralNode IntegerMath IntegerRemainderNode InternalForm InternalUtils Interned InternedDistinct InterningAnnotatedTypeFactory InterningChecker InterningVisitor InternMethod IntRange IntRangeFromGTENegativeOne IntRangeFromNonNegative IntRangeFromPositive IntVal InvalidFormat InvisibleQualifier Invoke IrrelevantTypeAnnotator JarEntryAnnotationFileResource JavaCodeStatistics JavaExpression JavaExpression JavaExpressionConverter JavaExpressionOptimizer JavaExpressionParseUtil JavaExpressionParseUtil.JavaExpressionParseException JavaExpressionScanner JavaExpressionVisitor JavaParserUtil JavaParserUtil.StringLiteralConcatenateVisitor JavaStubifier JointJavacJavaParserVisitor JointVisitorWithDefaultAction K KeyFor KeyForAnalysis KeyForAnnotatedTypeFactory KeyForAnnotatedTypeFactory.KeyForTypeHierarchy KeyForBottom KeyForPropagationTreeAnnotator KeyForPropagator KeyForPropagator.PropagationDirection KeyForStore KeyForSubchecker KeyForTransfer KeyForValue kg km km2 km3 kmPERh kN Label LambdaResultExpressionNode LeakedToResult LeftShiftNode Length LengthOf LessThan LessThanAnnotatedTypeFactory LessThanBottom LessThanChecker LessThanNode LessThanOrEqualNode LessThanTransfer LessThanUnknown LessThanVisitor ListTreeAnnotator ListTypeAnnotator LiteralKind LiteralTreeAnnotator LiveVariablePlayground LiveVarStore LiveVarTransfer LiveVarValue LocalizableKey LocalizableKeyAnnotatedTypeFactory LocalizableKeyBottom LocalizableKeyChecker Localized LocalVariable LocalVariableNode LockAnalysis LockAnnotatedTypeFactory LockChecker LockHeld LockingFree LockPossiblyHeld LockStore LockTransfer LockTreeAnnotator LockVisitor LombokSupport LongLiteralNode LongMath LowerBoundAnnotatedTypeFactory LowerBoundBottom LowerBoundChecker LowerBoundTransfer LowerBoundUnknown LowerBoundVisitor LTEqLengthOf LTLengthOf LTOMLengthOf Luminance m m2 m3 MarkerNode Mass MatchesRegex MaybeAliased MaybeLeaked MaybePresent MayReleaseLocks MethodAccessNode MethodApplier MethodCall MethodDescriptor MethodInvocationNode MethodTypeParamApplier MethodVal MethodValAnnotatedTypeFactory MethodValBottom MethodValChecker MethodValVisitor min MinLen MinLenFieldInvariant MixedUnits mm mm2 mm3 mol MonotonicNonNull MonotonicQualifier MostlyNoElementQualifierHierarchy MostlySingleton mPERs mPERs2 MustCall MustCallAlias MustCallAnnotatedTypeFactory MustCallChecker MustCallInferenceLogic MustCallNoCreatesMustCallForChecker MustCallTransfer MustCallTypeAnnotator MustCallUnknown MustCallVisitor N NarrowingConversionNode NegativeIndexFor NewInstance NewObject Node NoDefaultQualifierForUse NodeUtils NodeVisitor NoElementQualifierHierarchy NonLeaked NonNegative NonNull NoQualifierParameter NotCalledMethods NotEqualNode NotOnlyInitialized NotOwning Nullable NullChkNode NullLiteralNode NullnessAnalysis NullnessAnnotatedTypeFactory NullnessAnnotatedTypeFactory.NullnessPropagationTreeAnnotator NullnessAnnotatedTypeFormatter NullnessAnnotatedTypeFormatter.NullnessFormattingVisitor NullnessChecker NullnessStore NullnessTransfer NullnessUtil NullnessValue NullnessVisitor NumberMath NumberUtils NumericalAdditionNode NumericalMinusNode NumericalMultiplicationNode NumericalPlusNode NumericalSubtractionNode ObjectCreationNode OffsetDependentTypesHelper OffsetEquation Opt OptionalBottom OptionalChecker OptionalVisitor OptionConfiguration Owning PackageNameNode Pair ParamApplier ParameterizedTypeNode PartialRegex PhaseOneResult PolyFenum PolyIndex PolyInitializedFields PolyInterned PolyKeyFor PolyLength PolyLowerBound PolymorphicQualifier PolyMustCall PolyNull PolyPresent PolyRegex PolySameLen PolySignature PolySigned PolyTainted PolyUI PolyUIEffect PolyUIType PolyUnit PolyUpperBound PolyValue Positive PostconditionAnnotation PreconditionAnnotation Prefix Present PrimitiveType PrimitiveTypeNode PropagationTreeAnnotator PropagationTypeAnnotator PropertyKey PropertyKeyAnnotatedTypeFactory PropertyKeyBottom PropertyKeyChecker Pure Pure Pure.Kind PurityAnnotatedTypeFactory PurityChecker PurityChecker PurityChecker.PurityCheckerHelper PurityChecker.PurityResult PurityUnqualified PurityUtils QualifierArgument QualifierDefaults QualifierDefaults.BoundType QualifierForLiterals QualifierHierarchy QualifierKind QualifierKindHierarchy QualifierPolymorphism QualifierUpperBounds radians Range ReflectionResolver ReflectiveEvaluator Regex RegexAnnotatedTypeFactory RegexBottom RegexChecker RegexTransfer RegexUtil RegexUtil.CheckedPatternSyntaxException RegexVisitor RegularBlock RegularBlockImpl RegularTransferResult ReleasesNoLocks RelevantJavaTypes RemoveAnnotationsForInference ReportCall ReportChecker ReportCreation ReportInherit ReportOverride ReportReadWrite ReportUnqualified ReportUse ReportVisitor ReportWrite RequiresCalledMethods RequiresCalledMethods.List RequiresNonNull RequiresNonNull.List RequiresQualifier RequiresQualifier.List Resolver ResourceLeakAnalysis ResourceLeakAnnotatedTypeFactory ResourceLeakChecker ResourceLeakTransfer ResourceLeakVisitor ReturnNode ReturnsFormat ReturnsReceiver ReturnsReceiverAnnotatedTypeFactory ReturnsReceiverChecker ReturnsReceiverVisitor s SafeEffect SafeType SameLen SameLenAnnotatedTypeFactory SameLenBottom SameLenChecker SameLenTransfer SameLenUnknown SameLenVisitor SceneToStubWriter SearchIndexAnnotatedTypeFactory SearchIndexBottom SearchIndexChecker SearchIndexFor SearchIndexTransfer SearchIndexUnknown ShortLiteralNode ShortMath SideEffectFree SignatureAnnotatedTypeFactory SignatureBottom SignatureChecker SignaturePrinter SignatureTransfer SignatureUnknown Signed SignednessAnnotatedTypeFactory SignednessBottom SignednessChecker SignednessGlb SignednessUtil SignednessUtilExtra SignednessVisitor SignedPositive SignedPositiveFromUnsigned SignedRightShiftNode SimpleAnnotatedTypeScanner SimpleAnnotatedTypeScanner.DefaultAction SimpleAnnotatedTypeVisitor SingleSuccessorBlock SingleSuccessorBlockImpl SourceChecker SourceVisitor SpecialBlock SpecialBlock.SpecialBlockType SpecialBlockImpl Speed StaticallyExecutable Store Store.FlowRule Store.Kind StringCFGVisualizer StringConcatenateAssignmentNode StringConcatenateNode StringConversionNode StringLiteralNode StringToJavaExpression StringVal StructuralEqualityComparer StructuralEqualityVisitHistory StubFiles StubGenerator Subsequence Substance SubstringIndexAnnotatedTypeFactory SubstringIndexBottom SubstringIndexChecker SubstringIndexFor SubstringIndexUnknown SubtypeIsSubsetQualifierHierarchy SubtypeIsSupersetQualifierHierarchy SubtypeOf SubtypesSolver SubtypeVisitHistory SubtypingAnnotatedTypeFactory SubtypingAnnotationClassLoader SubtypingChecker SuperNode SuperTypeApplier SupertypesSolver SupportedLintOptions SupportedOptions SuppressWarningsPrefix SwingBoxOrientation SwingCompassDirection SwingElementOrientation SwingHorizontalOrientation SwingSplitPaneOrientation SwingTextOrientation SwingTitleJustification SwingTitlePosition SwingVerticalOrientation SwitchExpressionNode SwitchExpressionScanner SwitchExpressionScanner.FunctionalSwitchExpressionScanner SynchronizedNode SyntheticArrays SystemGetPropertyHandler SystemUtil t Tainted TaintingChecker TaintingVisitor TargetConstraints TargetConstraints.Equalities TargetConstraints.Subtypes TargetConstraints.Supertypes TargetLocations Temperature TerminatesExecution TernaryExpressionNode This ThisNode ThisReference ThrowNode Time TIsU ToIndexFileConverter TransferFunction TransferInput TransferResult TreeAnnotator TreeBuilder TreeDebug TreeDebug.Visitor TreeParser TreePathCacher TreePathUtil TreePrinter TreeScannerWithDefaults TreeUtils TSubU TSuperU TUConstraint TypeAnnotationMover TypeAnnotationUtils TypeAnnotator TypeArgInferenceUtil TypeArgumentInference TypeArgumentMapper TypeCastNode TypeDeclarationApplier TypeHierarchy TypeKind TypeKindUtils TypeKindUtils.PrimitiveConversionKind TypeOutputtingChecker TypeOutputtingChecker.GeneralAnnotatedTypeFactory TypeOutputtingChecker.Visitor TypesIntoElements TypesUtils TypeSystemError TypeUseLocation TypeValidator TypeVariableSubstitutor TypeVarUseApplier TypeVisualizer UBQualifier UBQualifier.LessThanLengthOf UBQualifier.UpperBoundLiteralQualifier UBQualifier.UpperBoundUnknownQualifier UI UIEffect UIPackage UIType UnaryOperation UnaryOperationNode UnderInitialization UnderlyingAST UnderlyingAST.CFGLambda UnderlyingAST.CFGMethod UnderlyingAST.CFGStatement UnderlyingAST.Kind Unique UnitsAnnotatedTypeFactory UnitsAnnotatedTypeFactory.UnitsQualifierKindHierarchy UnitsAnnotatedTypeFormatter UnitsAnnotatedTypeFormatter.UnitsAnnotationFormatter UnitsAnnotatedTypeFormatter.UnitsFormattingVisitor UnitsAnnotationClassLoader UnitsBottom UnitsChecker UnitsMultiple UnitsRelations UnitsRelations UnitsRelationsDefault UnitsRelationsTools UnitsTools UnitsVisitor Unknown UnknownClass UnknownCompilerMessageKey UnknownFormat UnknownInitialization UnknownInterned UnknownKeyFor UnknownLocalizableKey UnknownLocalized UnknownMethod UnknownPropertyKey UnknownRegex UnknownSignedness UnknownThis UnknownUnits UnknownVal UnmodifiableIdentityHashMap Unqualified Unsigned UnsignedRightShiftNode Untainted Unused UpperBoundAnnotatedTypeFactory UpperBoundBottom UpperBoundChecker UpperBoundFor UpperBoundLiteral UpperBoundTransfer UpperBoundUnknown UpperBoundVisitor UserError UsesObjectEquals ValueAnnotatedTypeFactory ValueChecker ValueCheckerUtils ValueLiteral ValueLiteralNode ValueTransfer ValueVisitor VariableApplier VariableDeclarationNode ViewpointAdaptJavaExpression VoidVisitorWithDefaultAction Volume WholeProgramInference WholeProgramInference.OutputFormat WholeProgramInferenceImplementation WholeProgramInferenceJavaParserStorage WholeProgramInferenceScenesStorage WholeProgramInferenceScenesStorage.AnnotationsInContexts WholeProgramInferenceStorage WideningConversionNode";

  /**
   * This method eliminates a possible ']' symbol or ',' symbol at the end of an annotation. When we
   * convert the delta from the Diff algorithm into a string and read that string word by word, the
   * annotation that we encounter might have one additional ] or , symbol at the end.
   *
   * <p>Here is a sample output of the Diff algorithm: [ChangeDelta, position: 2, lines:
   * [ @NonNull, @SideEffectFree] to [ , ]]. So we will have @NonNull, and @SideEffectFree] here.
   *
   * @param anno an annotation from the delta of the Diff algorithm, either in the @AnnoName]
   *     or @AnnoName, format
   * @return the annotation itself without any "]" symbol
   */
  private static String trimAnnotation(@MinLen(2) String anno) {
    String result = anno;
    @SuppressWarnings(
        "index:assignment") // This method should only be called on strings that contain an "@"
    @NonNegative int index1 = result.indexOf('@');
    /* if apart from the '@' symbol, the anno contains only alphabetical elements (for example: @NulLable), we will take
    the whole string. Otherwise, for cases such as @Nullable], we will ignore the last element of the anno.
    */
    int indexConsider = result.indexOf("]");
    if (indexConsider == -1) {
      result = result.substring(index1, result.length());
    } else {
      result = result.substring(index1, indexConsider);
    }
    int indexConsider2 = result.indexOf(',');
    if (indexConsider2 == -1) {
      result = result.substring(index1, result.length());
    } else {
      result = result.substring(index1, indexConsider2);
    }
    return result;
  }

  /**
   * This method takes a line, which contains at least one annotation, and return the first
   * annotation in that line.
   *
   * @param line a non-empty line containing at least one annotation
   * @return the annotation which the line begins with
   */
  private static String getAnnos(String line) {
    String[] temp = line.split(" ");
    String result = "";
    for (String word : temp) {
      if (word.length() >= 1) {
        if (word.charAt(0) == '@') {
          result = word.substring(1, word.length());
          break;
        }
      }
    }
    return result;
  }

  /**
   * This method counts the number of annotations in a line
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
   * This method checks if a particular index in a line belongs to a string literal
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
   * This method trims out all the comments in a line from the input files
   *
   * @param line a line from the input files
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
    if (indexStar != -1) {
      finalLine = line.substring(0, indexStar);
    }
    return finalLine;
  }

  /**
   * This method formats a line that may or may not contain fully-qualified annotation. The way this
   * method formats that line is to change all annotations written in the fully-qualified format to
   * the simple format. For example, changing "@org.checkerframework.dataflow.qual.Pure" to "@Pure."
   * This method should be applied before passing a line to the Diff algorithm.
   *
   * @param line a line that belongs to the input files
   * @return the same line with all the annotations being changed to the simple format.
   */
  private static String extractCheckerPackage(String line) {
    String[] temp = line.split(" ");
    String result = line;
    if (line.length() != 0) {
      for (String word : temp) {
        if (word.contains("@org.checkerframework")) {
          String[] tempo = word.split("[.]");
          String tempResult = "@" + tempo[tempo.length - 1];
          @SuppressWarnings(
              "index:assignment") /* "temp" is an array created by splitting the string "result". As "word"
                                  is an element of the array "temp", it is also contained in the string result. So "begin" can not be negative. */
          @NonNegative int begin = result.indexOf(word);
          int end = begin + word.length();
          String firstPart = result.substring(0, begin);
          String secondPart = result.substring(end, result.length());
          result = firstPart + tempResult + secondPart;
        }
      }
    }
    return result;
  }

  /**
   * This method trim out the parenthesized part in an annotation, for example, @Annotation(abc)
   * will be changed to @Annotation.
   *
   * <p>This method need to be used with care. We want to use it to update the final result. This
   * method should not be used for any list or string that will become the input of the Diff
   * algorithm. If we do that, the Diff algorithm will not be able to recognize any potential
   * difference in the parentheses between an annotation written by human and an annotation
   * generated by the computer anymore.
   *
   * @param anno the annotation which will be trimmed
   * @return that annotation without the parenthesized part
   */
  private static String trimParen(String anno) {
    int para = anno.indexOf("(");
    if (para == -1) {
      return anno;
    }
    return anno.substring(0, para);
  }

  /**
   * This method returns a List containing all the annotations belonging to a line.
   *
   * @param str a line
   * @return a Linked List containing all annotations of str.
   */
  private static List extractString(String str) {
    List<String> result = new ArrayList<String>();
    int countAnno = countAnnos(str);
    String temp = str;
    for (int i = 0; i < countAnno; i++) {
      int index1 = temp.indexOf('@');
      if (index1 == -1) {
        throw new RuntimeException(
            "The extractString method relies on the countAnnos method. Either the countAnnos method is wrong"
                + "or it was not called properly");
      }
      String tempAnno = getAnnos(temp);
      if (checkInString(index1, temp) && checkerFramworkPackage.contains(tempAnno)) {
        if (tempAnno.contains("(")) {
          if (temp.contains(")")) {
            tempAnno = temp.substring(index1, temp.indexOf(')') + 1);
          } else {
            tempAnno = temp.substring(index1, temp.length());
          }
          result.add(tempAnno);
        } else {
          result.add(tempAnno);
        }
      }
      temp = temp.substring(index1 + 1, temp.length());
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
    File file;
    // This is to be used after successfully implementing the command line to update
    // type-qualifiers.txt
    /*List<String> checkerPackage=new ArrayList<String>();
    file = new File("type-qualifiers.txt");
    try (FileReader fr = new FileReader(file)) {
      BufferedReader br = new BufferedReader(fr);
      String str;
      while ((str = br.readLine()) != null) {
        checkerPackage.add(str);
      }
    } catch (Exception e) {
      throw new RuntimeException("Could not read type-qualifiers.txt, check if it exists?");
    }
    */
    List<String> originalFile = new ArrayList<String>();
    // specific annotations and the number of computer-written files missing them
    Map<String, Integer> AnnoLocate = new HashMap<String, Integer>();
    // the name of the types of annotation and their numbers in the human-written file
    Map<String, Integer> AnnoCount = new HashMap<String, Integer>();
    /* the name of the types of annotations and their "correct" numbers (meaning the number of annotations of that
    type not missed by computer-written files) */
    Map<String, Integer> AnnoSimilar = new HashMap<String, Integer>();
    int pos;
    file = new File(args[0]);
    try (FileReader fr = new FileReader(file)) {
      BufferedReader br = new BufferedReader(fr);
      String str;
      pos = -1;
      List<String> annoList = new ArrayList<String>();
      while ((str = br.readLine()) != null) {
        pos++;
        str = ignoreComment(str);
        str = extractCheckerPackage(str);
        originalFile.add(str);
        annoList = extractString(str);
        for (String anno : annoList) {
          String annoNoPara = trimParen(anno);
          if (checkerFramworkPackage.contains(annoNoPara)) {
            String finalAnno = "@" + annoNoPara;
            if (AnnoCount.containsKey(finalAnno)) {
              System.out.println(finalAnno);
              int numberOfAnno = AnnoCount.get(finalAnno);
              AnnoCount.put(finalAnno, numberOfAnno + 1);
            } else {
              AnnoCount.put(finalAnno, new Integer(1));
            }
            AnnoSimilar.put(finalAnno, new Integer(0));
            // we want the keys in the map AnnoLocate has this following format: type_position
            String posi = String.valueOf(pos);
            AnnoLocate.put("@" + anno + "_" + posi, new Integer(0));
          }
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
      for (AbstractDelta<String> delta : patch.getDeltas()) {
        // get the delta in string format
        String deltaInString = delta.toString();
        String newpos = "";
        // we change the delta output to a string, then break that string into different parts
        List<String> myList = new ArrayList<String>(Arrays.asList(deltaInString.split(" ")));
        // just take the delta with annotations into consideration
        if (deltaInString.contains("@")) {
          // get the position of that annotation in the delta, which is something like "5," or "6,".
          String posi = myList.get(2);
          // take the "," out
          if (posi.length() > 1) {
            newpos = posi.substring(0, posi.length() - 1);
          }
          String result = "";
          for (String element : myList) {
            if (element.contains("@")) {
              if (element.length() > 2) {
                element = trimAnnotation(element);
                int currLine = Integer.parseInt(newpos);
                // to match the one in AnnoLocate
                result = element + "_" + newpos;
                System.out.println(result);
                // update the data of AnnoLocate
                if (AnnoLocate.containsKey(result)) {
                  int value = AnnoLocate.get(result);
                  AnnoLocate.put(result, value + 1);
                } else {
                  while (currLine < pos) {
                    currLine++;
                    result = element + "_" + currLine;
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
        annoName = trimParen(annoName);
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
  }
}
