import { Component } from '@angular/core';

export interface FAQ {
  id: string;
  question: string;
  answer: string;
  expanded: boolean;
}

export interface Allergen {
  name: string;
  image: string;
}

export interface Testimonial {
  quote: string;
  author: string;
  role: string;
  image: string;
}

@Component({
  selector: 'app-faqs',
  templateUrl: './faqs.component.html',
  styleUrls: ['./faqs.component.css']
})
export class FaqsComponent {

  faqs: FAQ[] = [
    {
      id: 'one',
      question: 'How do I make a reservation?',
      answer: 'You can make a reservation by calling us directly or using our online booking system on the website.',
      expanded: false
    },
    {
      id: 'two',
      question: 'Do you offer vegetarian options?',
      answer: 'Yes, we have a variety of vegetarian dishes available on our menu.',
      expanded: false
    },
    {
      id: 'three',
      question: 'What are your opening hours?',
      answer: 'We are open from 9 AM to 9 PM from Monday to Saturday, and from 10 AM to 8 PM on Sundays.',
      expanded: false
    },
    {
      id: 'four',
      question: 'Do you offer gluten-free options?',
      answer: 'Yes, we have several gluten-free options available. Please inform our staff about any dietary restrictions when ordering.',
      expanded: false
    },
    {
      id: 'five',
      question: 'Do you have outdoor seating?',
      answer: 'Yes, we have a cozy outdoor seating area where you can enjoy your meal in a relaxed atmosphere.',
      expanded: false
    },
    {
      id: 'six',
      question: 'Is there parking available?',
      answer: 'Yes, we offer free parking for our customers. There is also street parking available nearby.',
      expanded: false
    },
    {
      id: 'seven',
      question: 'Do you offer takeaway and delivery?',
      answer: 'Yes, we offer both takeaway and delivery services. You can order through our website or via our delivery partners.',
      expanded: false
    },
    {
      id: 'eight',
      question: 'Can I host a private event at your restaurant?',
      answer: 'Absolutely! We offer private event bookings for birthdays, corporate gatherings, and other celebrations. Contact us for more details.',
      expanded: false
    },
    {
      id: 'nine',
      question: 'Do you have a kids\' menu?',
      answer: 'Yes, we offer a special kids\' menu with a variety of delicious and nutritious options for our younger guests.',
      expanded: false
    },
    {
      id: 'ten',
      question: 'Can I bring my pet to the restaurant?',
      answer: 'Pets are welcome in our outdoor seating area! However, we kindly ask that they remain on a leash and behave appropriately.',
      expanded: false
    },
    {
      id: 'eleven',
      question: 'Do you offer gift cards?',
      answer: 'Yes, we offer gift cards in different denominations. They make a perfect gift for any occasion!',
      expanded: false
    },
    {
      id: 'twelve',
      question: 'Is Wi-Fi available at the restaurant?',
      answer: 'Yes, we offer free Wi-Fi for our guests. Just ask our staff for the login details.',
      expanded: false
    }
  ];


  allergens: Allergen[] = [
    { name: 'Lupin', image: 'assets/img/allergen_symbols/allergen_symbol_altramuz.png' },
    { name: 'Celery', image: 'assets/img/allergen_symbols/allergen_symbol_apio.png' },
    { name: 'Gluten', image: 'assets/img/allergen_symbols/allergen_symbol_cereal.png' },
    { name: 'Crustaceans', image: 'assets/img/allergen_symbols/allergen_symbol_crustacean.png' },
    { name: 'Dairy', image: 'assets/img/allergen_symbols/allergen_symbol_dairy.png' },
    { name: 'Eggs', image: 'assets/img/allergen_symbols/allergen_symbol_eggs.png' },
    { name: 'Fish', image: 'assets/img/allergen_symbols/allergen_symbol_fish.png' },
    { name: 'Mollusks', image: 'assets/img/allergen_symbols/allergen_symbol_mollusks.png' },
    { name: 'Mustard', image: 'assets/img/allergen_symbols/allergen_symbol_mustard.png' },
    { name: 'Nuts', image: 'assets/img/allergen_symbols/allergen_symbol_nuts.png' },
    { name: 'Peanuts', image: 'assets/img/allergen_symbols/allergen_symbol_peanut.png' },
    { name: 'Sesame', image: 'assets/img/allergen_symbols/allergen_symbol_sesame.png' },
    { name: 'Soy', image: 'assets/img/allergen_symbols/allergen_symbol_soybeans.png' },
    { name: 'Sulfites', image: 'assets/img/allergen_symbols/allergen_symbol_sulfites.png' }
  ];


  testimonials: Testimonial[] = [
    {
      quote: "The croquettes here are simply amazing! Crispy on the outside, creamy on the inside, and packed with flavor. A must-visit spot!",
      author: "Sophie Martinez",
      role: "Food Blogger",
      image: "assets/img/testimonial-1.jpg"
    },
    {
      quote: "I loved the variety of croquettes! From the classic ham to the innovative truffle and mushroom, each bite was delicious. The ambiance is also fantastic!",
      author: "Daniel Herrera",
      role: "Chef",
      image: "assets/img/testimonial-2.jpg"
    },
    {
      quote: "Best croquettes I've ever had! Perfectly cooked and full of flavor. The staff was friendly, and the service was top-notch. Highly recommend!",
      author: "Laura Evans",
      role: "Tourist",
      image: "assets/img/testimonial-3.jpg"
    },
    {
      quote: "Great experience! The croquettes were freshly made, and the flavors were incredible. The perfect place for a casual yet delicious meal!",
      author: "Carlos Méndez",
      role: "Local Guide",
      image: "assets/img/testimonial-4.jpg"
    }
  ];


  toggleAccordion(faq: FAQ): void {

    if (!faq.expanded) {
      this.faqs.forEach(item => {
        if (item.id !== faq.id) {
          item.expanded = false;
        }
      });
    }

    faq.expanded = !faq.expanded;
  }


  trackByFaqId(index: number, faq: FAQ): string {
    return faq.id;
  }

  trackByAllergenName(index: number, allergen: Allergen): string {
    return allergen.name;
  }

  trackByTestimonialAuthor(index: number, testimonial: Testimonial): string {
    return testimonial.author;
  }
}
