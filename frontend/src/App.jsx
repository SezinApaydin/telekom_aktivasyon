import { useEffect, useState } from 'react';
import Step1 from './components/Step1';
import Step2 from './components/Step2';

function App() {
  const [cartId, setCartId] = useState(null);
  const [step, setStep] = useState(1);

  useEffect(() => {
    fetch('http://localhost:8080/api/cart?customerIdentifier=web-musteri-' + Date.now(), {
      method: 'POST',
    })
        .then((res) => res.json())
        .then((data) => setCartId(data.id));
  }, []);

  if (!cartId) {
    return <p>Sepet oluşturuluyor...</p>;
  }

  return (
      <div style={{ maxWidth: 700, margin: '40px auto', fontFamily: 'sans-serif' }}>
        <h1>Hat Aktivasyon Başvurusu</h1>
        {step === 1 && <Step1 cartId={cartId} onNext={() => setStep(2)} />}
        {step === 2 && <Step2 cartId={cartId} onNext={() => setStep(3)} onBack={() => setStep(1)} />}
        {step === 3 && <p>Adım 3 (adres formu) — Gün 19'da ekleyeceğiz.</p>}
      </div>
  );
}

export default App;