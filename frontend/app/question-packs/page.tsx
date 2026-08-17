"use client";
import { useEffect, useState } from "react";
import { useRouter } from "next/navigation";
import Link from "next/link";
import { apiFetch } from "@/lib/api";
import { QuestionPackTemplateSummary } from "@/lib/types";
import Header from "@/components/Header";

const targetTypeLabel: Record<string, string> = {
  PARTNER: "연인",
  FAMILY: "가족",
  SELF: "나에게",
};

export default function QuestionPacksPage() {
  const [templates, setTemplates] = useState<QuestionPackTemplateSummary[]>([]);
  const [loading, setLoading] = useState(true);
  const router = useRouter();

  useEffect(() => {
    apiFetch("/api/question-pack-templates")
      .then(setTemplates)
      .finally(() => setLoading(false));
  }, []);

  return (
    <>
      <Header />

      <main className="mx-auto max-w-3xl py-16">
        <p className="text-sm font-medium text-rose-500">질문팩 선택</p>
        <h1 className="mt-2 text-3xl font-bold">누구를 위한 기록인가요?</h1>
        <p className="mt-3 text-gray-500">
          질문팩을 고르면 그에 맞는 문항으로 시작해요. 나중에 자유롭게 수정할 수 있어요.
        </p>

        {loading ? (
          <p className="mt-12 text-sm text-gray-400">불러오는 중...</p>
        ) : (
          <div className="mt-10 grid grid-cols-1 gap-4 sm:grid-cols-2">
            {templates.map((t) => (
              <button
                key={t.id}
                onClick={() => router.push(`/question-packs/${t.id}/setup`)}
                className="rounded-2xl border border-gray-200 p-6 text-left transition hover:border-gray-900 hover:shadow-sm"
              >
                <span className="inline-block rounded-full bg-rose-50 px-3 py-1 text-xs font-medium text-rose-500">
                  {targetTypeLabel[t.targetType] ?? t.targetType}
                </span>
                <p className="mt-4 text-lg font-medium">{t.name}</p>
              </button>
            ))}

            <button
              onClick={() => router.push("/question-packs/custom/setup")}
              className="rounded-2xl border border-dashed border-gray-300 p-6 text-left text-gray-500 transition hover:border-gray-900 hover:text-gray-900"
            >
              <span className="inline-block rounded-full bg-gray-50 px-3 py-1 text-xs font-medium text-gray-400">
                직접 만들기
              </span>
              <p className="mt-4 text-lg font-medium">완전히 새로 만들기</p>
            </button>
          </div>
        )}
      </main>
    </>
  );
}