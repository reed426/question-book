"use client";
import { useEffect, useState } from "react";
import Link from "next/link";
import Header from "@/components/Header";
import { apiFetch } from "@/lib/api";
import { QuestionSetSummary } from "@/lib/types";

const targetTypeLabel: Record<string, string> = {
  PARTNER: "연인",
  FAMILY: "가족",
  SELF: "나에게",
};

export default function MyQuestionSetsPage() {
  const [sets, setSets] = useState<QuestionSetSummary[]>([]);
  const [loading, setLoading] = useState(true);

  useEffect(() => {
    apiFetch("/api/question-sets")
      .then(setSets)
      .finally(() => setLoading(false));
  }, []);

  return (
    <>
      <Header />
      <main className="mx-auto max-w-3xl px-8 py-16">
        <div className="flex items-center justify-between">
          <h1 className="text-2xl font-bold">내 질문북</h1>
          <Link
            href="/question-packs"
            className="rounded-full bg-gray-900 px-4 py-2 text-sm text-white hover:bg-gray-700"
          >
            + 새로 만들기
          </Link>
        </div>

        {loading ? (
          <p className="mt-12 text-sm text-gray-400">불러오는 중...</p>
        ) : sets.length === 0 ? (
          <p className="mt-12 text-sm text-gray-500">아직 만든 질문북이 없어요.</p>
        ) : (
          <div className="mt-8 space-y-3">
            {sets.map((s) => {
              const percentage = s.total === 0 ? 0 : Math.round((s.answered / s.total) * 100);
              return (
                <Link
                  key={s.id}
                  href={`/question-sets/${s.id}`}
                  className="block rounded-lg border border-gray-200 p-5 hover:border-gray-900"
                >
                  <div className="flex items-center justify-between">
                    <div>
                      {s.targetType && (
                        <span className="inline-block rounded-full bg-rose-50 px-3 py-1 text-xs font-medium text-rose-500">
                          {targetTypeLabel[s.targetType] ?? s.targetType}
                        </span>
                      )}
                      <p className="mt-2 font-medium">{s.title}</p>
                    </div>
                    <p className="text-sm text-gray-400">
                      {s.answered} / {s.total} ({percentage}%)
                    </p>
                  </div>
                </Link>
              );
            })}
          </div>
        )}
      </main>
    </>
  );
}