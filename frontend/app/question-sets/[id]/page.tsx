"use client";
import { useEffect, useState } from "react";
import { useParams } from "next/navigation";
import Link from "next/link";
import { apiFetch } from "@/lib/api";
import { QuestionSetResponse, QuestionResponse } from "@/lib/types";

export default function QuestionSetHomePage() {
  const params = useParams();
  const questionSetId = params.id as string;

  const [data, setData] = useState<QuestionSetResponse | null>(null);
  const [loading, setLoading] = useState(true);
  const [newQuestionText, setNewQuestionText] = useState("");
  const [editingId, setEditingId] = useState<number | null>(null);
  const [editingText, setEditingText] = useState("");

  const load = () => {
    apiFetch(`/api/question-sets/${questionSetId}`)
      .then(setData)
      .finally(() => setLoading(false));
  };

  useEffect(() => {
    load();
  }, [questionSetId]);

  if (loading || !data) return <p className="px-8 py-16">불러오는 중...</p>;

  const total = data.questions.length;
  const answered = data.questions.filter((q) => q.answered).length;
  const percentage = total === 0 ? 0 : Math.round((answered / total) * 100);

  const handleAdd = async () => {
    if (!newQuestionText.trim()) return;
    await apiFetch(`/api/question-sets/${questionSetId}/questions`, {
      method: "POST",
      body: JSON.stringify({ text: newQuestionText }),
    });
    setNewQuestionText("");
    load();
  };

  const startEdit = (q: QuestionResponse) => {
    setEditingId(q.id);
    setEditingText(q.text);
  };

  const saveEdit = async (id: number) => {
    await apiFetch(`/api/questions/${id}`, {
      method: "PATCH",
      body: JSON.stringify({ text: editingText }),
    });
    setEditingId(null);
    load();
  };

  const handleDelete = async (id: number) => {
    if (!confirm("이 질문을 삭제할까요?")) return;
    await apiFetch(`/api/questions/${id}`, { method: "DELETE" });
    load();
  };

  return (
    <main className="mx-auto max-w-2xl px-8 py-16">
      <div className="flex items-center justify-between">
        <h1 className="text-2xl font-bold">나의 질문 목록</h1>
        <Link href={`/question-sets/${questionSetId}/preview`} className="text-sm text-gray-500 underline">
          북 미리보기 →
        </Link>
      </div>

      <div className="mt-6">
        <div className="h-2 w-full overflow-hidden rounded-full bg-gray-100">
          <div className="h-full rounded-full bg-rose-400" style={{ width: `${percentage}%` }} />
        </div>
        <p className="mt-2 text-sm text-gray-500">
          {answered} / {total} 완료 ({percentage}%)
        </p>
      </div>

      <ul className="mt-8 space-y-3">
        {data.questions.map((q) => (
          <li
            key={q.id}
            className={`rounded-lg border p-4 ${q.locked ? "border-gray-100 bg-gray-50" : "border-gray-200"}`}
          >
            {editingId === q.id ? (
              <div className="flex items-center gap-2">
                <input
                  value={editingText}
                  onChange={(e) => setEditingText(e.target.value)}
                  className="flex-1 rounded border border-gray-200 px-3 py-2 text-sm"
                />
                <button onClick={() => saveEdit(q.id)} className="text-sm text-gray-900 underline">
                  저장
                </button>
                <button onClick={() => setEditingId(null)} className="text-sm text-gray-400">
                  취소
                </button>
              </div>
            ) : (
              <div className="flex items-center justify-between gap-4">
                <div>
                  {q.locked ? (
                    <p className="text-sm text-gray-400">🔒 {q.text}</p>
                  ) : (
                    <Link
                      href={`/question-sets/${questionSetId}/questions/${q.id}/answer`}
                      className="text-sm hover:underline"
                    >
                      {q.text}
                    </Link>
                  )}
                  {q.answered && <span className="ml-2 text-xs text-rose-400">답변 완료</span>}
                </div>
                {!q.locked && (
                  <div className="flex shrink-0 gap-2 text-xs text-gray-400">
                    <button onClick={() => startEdit(q)} className="hover:text-gray-900">
                      수정
                    </button>
                    <button onClick={() => handleDelete(q.id)} className="hover:text-red-500">
                      삭제
                    </button>
                  </div>
                )}
              </div>
            )}
          </li>
        ))}
      </ul>

      <div className="mt-8 flex gap-2">
        <input
          value={newQuestionText}
          onChange={(e) => setNewQuestionText(e.target.value)}
          placeholder="새 질문 추가하기"
          className="flex-1 rounded-lg border border-gray-200 px-4 py-3 text-sm"
        />
        <button
          onClick={handleAdd}
          className="rounded-lg bg-gray-900 px-4 py-3 text-sm text-white hover:bg-gray-700"
        >
          추가
        </button>
      </div>
    </main>
  );
}