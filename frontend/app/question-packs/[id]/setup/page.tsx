"use client";
import { useEffect, useState } from "react";
import { useParams, useRouter } from "next/navigation";
import { apiFetch } from "@/lib/api";
import { QuestionPackTemplateDetail, QuestionSetResponse } from "@/lib/types";

export default function SetupPage() {
  const params = useParams();
  const router = useRouter();
  const templateId = params.id as string;
  const isCustom = templateId === "custom";

  const [template, setTemplate] = useState<QuestionPackTemplateDetail | null>(null);
  const [mode, setMode] = useState<"FREE" | "PERIODIC">("FREE");
  const [intervalDays, setIntervalDays] = useState(7);
  const [startDate, setStartDate] = useState(() => new Date().toISOString().slice(0, 10));
  const [loading, setLoading] = useState(!isCustom);
  const [submitting, setSubmitting] = useState(false);
  const [error, setError] = useState("");

  useEffect(() => {
    if (isCustom) return;
    apiFetch(`/api/question-pack-templates/${templateId}`)
      .then(setTemplate)
      .finally(() => setLoading(false));
  }, [templateId, isCustom]);

  const handleSubmit = async () => {
    setSubmitting(true);
    setError("");
    try {
      const body = {
        templateId: isCustom ? null : Number(templateId),
        mode,
        intervalDays: mode === "PERIODIC" ? intervalDays : null,
        startDate,
      };
      const set: QuestionSetResponse = await apiFetch("/api/question-sets", {
        method: "POST",
        body: JSON.stringify(body),
      });
      router.push(`/question-sets/${set.id}`);
    } catch (err) {
      setError(err instanceof Error ? err.message : "생성에 실패했습니다");
    } finally {
      setSubmitting(false);
    }
  };

  if (loading) return <p>불러오는 중...</p>;

  return (
    <main className="mx-auto max-w-xl px-8 py-16">
      <h1 className="text-2xl font-bold">{isCustom ? "완전히 새로 만들기" : template?.name}</h1>

      <div className="mt-8">
        <p className="mb-2 text-sm font-medium">답변 방식</p>
        <div className="flex gap-3">
          <button
            onClick={() => setMode("FREE")}
            className={`rounded-lg border px-4 py-3 text-sm ${
              mode === "FREE" ? "border-gray-900 bg-gray-900 text-white" : "border-gray-200"
            }`}
          >
            자유형 — 아무 질문이나 원할 때 답하기
          </button>
          <button
            onClick={() => setMode("PERIODIC")}
            className={`rounded-lg border px-4 py-3 text-sm ${
              mode === "PERIODIC" ? "border-gray-900 bg-gray-900 text-white" : "border-gray-200"
            }`}
          >
            주기형 — 순서대로 하나씩 열리기
          </button>
        </div>
      </div>

      {mode === "PERIODIC" && (
        <div className="mt-6">
          <label className="mb-2 block text-sm font-medium">며칠마다 새 질문이 열릴까요?</label>
          <input
            type="number"
            min={1}
            value={intervalDays}
            onChange={(e) => setIntervalDays(Number(e.target.value))}
            className="w-24 rounded-lg border border-gray-200 px-3 py-2"
          />
          <span className="ml-2 text-sm text-gray-500">일마다</span>
        </div>
      )}

      <div className="mt-6">
        <label className="mb-2 block text-sm font-medium">시작일</label>
        <input
          type="date"
          value={startDate}
          onChange={(e) => setStartDate(e.target.value)}
          className="rounded-lg border border-gray-200 px-3 py-2"
        />
      </div>

      {template && (
        <div className="mt-8">
          <p className="mb-2 text-sm font-medium">포함된 질문 {template.questions.length}개</p>
          <ul className="space-y-2 text-sm text-gray-500">
            {template.questions.map((q) => (
              <li key={q.id}>· {q.text}</li>
            ))}
          </ul>
          <p className="mt-2 text-xs text-gray-400">질문은 다음 화면에서 자유롭게 수정·추가·삭제할 수 있어요.</p>
        </div>
      )}

      {error && <p className="mt-4 text-sm text-red-500">{error}</p>}

      <button
        onClick={handleSubmit}
        disabled={submitting}
        className="mt-10 rounded-full bg-gray-900 px-6 py-3 text-white hover:bg-gray-700 disabled:opacity-50"
      >
        {submitting ? "만드는 중..." : "이 질문들로 시작하기"}
      </button>
    </main>
  );
}