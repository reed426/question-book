export interface TemplateQuestion {
  id: number;
  sortOrder: number;
  text: string;
}

export interface QuestionPackTemplateSummary {
  id: number;
  name: string;
  targetType: string;
}

export interface QuestionPackTemplateDetail extends QuestionPackTemplateSummary {
  questions: TemplateQuestion[];
}

export interface QuestionResponse {
  id: number;
  sortOrder: number;
  text: string;
  isCustom: boolean;
  locked: boolean;
  answered: boolean;
}

export interface QuestionSetResponse {
  id: number;
  mode: "FREE" | "PERIODIC";
  intervalDays: number | null;
  startDate: string;
  questions: QuestionResponse[];
}
export interface BookEntry {
  questionId: number;
  sortOrder: number;
  questionText: string;
  content: string;
  imageUrl: string | null;
  answeredAt: string;
}

export interface BookPreviewResponse {
  questionSetId: number;
  authorNickname: string;
  totalAnswered: number;
  entries: BookEntry[];
}

export interface QuestionSetSummary {
  id: number;
  title: string;
  targetType: string | null;
  mode: "FREE" | "PERIODIC";
  total: number;
  answered: number;
  createdAt: string;
}