"use client";
import Header from "@/components/Header";
import { useAuth } from "@/context/AuthContext";
import Link from "next/link";


export default function HomePage() {
  const { user } = useAuth();
  const ctaHref = user ? "/question-packs" : "/signup";
  return (
    <main className="min-h-screen bg-white text-gray-900">
      <Header/>

      <section className="relative mx-auto max-w-6xl overflow-hidden px-8 py-16 sm:py-20">
        <div className="absolute -left-20 top-0 -z-10 h-[500px] w-[500px] rounded-full bg-rose-50 blur-3xl" />
        <div className="absolute -right-10 bottom-0 -z-10 h-[400px] w-[400px] rounded-full bg-amber-50 blur-3xl" />

        <div className="grid grid-cols-1 items-center gap-12 sm:grid-cols-2">
          <div>
            <p className="mb-3 text-sm font-medium text-rose-500">질문에 답하며 쌓아가는 기록</p>
            <h1 className="text-4xl font-bold leading-tight sm:text-5xl">
              소중한 사람에게,
              <br />
              혹은 나에게
              <br />
              건네는 질문 하나
            </h1>
            <p className="mt-6 max-w-md text-gray-500">
              매주 하나씩 질문에 답하다 보면, <br/>어느새 나만의 이야기가 한 권의 책처럼 쌓여갑니다.
            </p>
            
            <Link
              href={ctaHref}
              className="mt-8 inline-block rounded-full bg-gray-900 px-6 py-3 text-white hover:bg-gray-700"
            >
              질문 받으러 가기
            </Link>
          </div>

          <div className="relative mx-auto w-full max-w-xs sm:max-w-sm">
            <img
              src="/main1.png"
              alt="책 표지 예시"
              className="aspect-[6/7] w-full rounded-[45%_40%_60%_55%/40%_60%_55%_45%] object-cover object-top"
            />
          </div>
        </div>
      </section>
      <section className="mx-auto max-w-4xl px-8 pb-32">
    <div className="grid grid-cols-1 gap-16 border-t border-gray-100 pt-16 sm:grid-cols-3">
      <div>
        <p className="font-serif text-2xl text-rose-300">01</p>
        <p className="mt-3 font-medium">질문팩을 골라요</p>
        <p className="mt-1 text-sm text-gray-500">연인, 가족, 혹은 나에게 건넬 질문을 선택해요.</p>
      </div>
      <div>
        <p className="font-serif text-2xl text-rose-300">02</p>
        <p className="mt-3 font-medium">글과 사진으로 답해요</p>
        <p className="mt-1 text-sm text-gray-500">원할 때마다, 또는 순서대로 하나씩 기록해요.</p>
      </div>
      <div>
        <p className="font-serif text-xl text-rose-300">03</p>
        <p className="mt-3 font-medium">책처럼 모아봐요</p>
        <p className="mt-1 text-sm text-gray-500">쌓인 기록을 미리보기로 넘겨보고, 링크로 공유해요.</p>
      </div>
    </div>
  </section>
    </main>
  );
}