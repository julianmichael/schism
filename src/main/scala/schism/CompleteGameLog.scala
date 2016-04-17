package schism

case class CompleteGameLog(annotator: String,
                           vocabulary: Set[String] ,
                           judgments: List[ComparativeJudgment],
                           answer: String)
