"use client";
import { useEffect, useState } from "react";
import Header from "@/components/Header";
import { apiFetch } from "@/lib/api";
import { AdminStatsResponse, CustomQuestionAdminView } from "@/lib/types";

export default function AdminPage() {
  const [stats, setStats] = useState<AdminStatsResponse | null>(null);
  const [error, setError] = useState(false);
  const [loading, setLoading] = useState(true);
  const [customQuestions, setCustomQuestions] = useState<CustomQuestionAdminView[]>([]);

  useEffect(() => {
  Promise.all([
    apiFetch("/api/admin/stats"),
    apiFetch("/api/admin/custom-questions"),
  ])
    .then(([statsData, customData]) => {
      setStats(statsData);
      setCustomQuestions(customData);
    })
    .catch(() => setError(true))
    .finally(() => setLoading(false));
}, []);

  if (loading) return <p className="px-8 py-16">불러오는 중...</p>;
  if (error || !stats) {
    return (
      <>
        <Header />
        <p className="px-8 py-16 text-center text-gray-500">관리자만 볼 수 있는 페이지예요.</p>
      </>
    );
  }

  return (
    <>
      <Header />
      <main className="mx-auto max-w-3xl px-8 py-16">
        <h1 className="text-2xl font-bold">운영 현황</h1>

        <div className="mt-8 grid grid-cols-2 gap-4 sm:grid-cols-4">
          <StatCard label="가입자 수" value={stats.totalUsers} />
          <StatCard label="질문북 수" value={stats.totalQuestionSets} />
          <StatCard label="총 답변 수" value={stats.totalAnswers} />
          <StatCard label="평균 완료율" value={`${stats.averageCompletionRate.toFixed(1)}%`} />
        </div>

        <h2 className="mt-12 text-lg font-medium">질문팩별 사용 현황</h2>
        <table className="mt-4 w-full text-sm">
          <tbody>
            {stats.templateUsage.map((t) => (
              <tr key={t.templateName} className="border-b border-gray-100">
                <td className="py-2 text-gray-700">{t.templateName}</td>
                <td className="py-2 text-right text-gray-400">{t.count}개</td>
              </tr>
            ))}
          </tbody>
        </table>

        <h2 className="mt-12 text-lg font-medium">사용자가 새로 추가한 질문</h2>
        <ul className="mt-4 space-y-3">
          {customQuestions.map((q) => (
            <li key={q.questionId} className="rounded-lg border border-gray-200 p-4">
              <p className="text-sm">{q.text}</p>
              <p className="mt-1 text-xs text-gray-400">
                {q.authorNickname} · {q.questionSetTitle} · {new Date(q.createdAt).toLocaleDateString("ko-KR")}
              </p>
            </li>
          ))}
          {customQuestions.length === 0 && (
            <p className="text-sm text-gray-400">아직 추가된 질문이 없어요.</p>
          )}
        </ul>
      </main>
    </>
  );
}

function StatCard({ label, value }: { label: string; value: string | number }) {
  return (
    <div className="rounded-lg border border-gray-200 p-4">
      <p className="text-xs text-gray-400">{label}</p>
      <p className="mt-1 text-xl font-bold">{value}</p>
    </div>
  );
}