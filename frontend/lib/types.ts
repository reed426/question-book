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