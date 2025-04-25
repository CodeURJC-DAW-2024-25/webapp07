import { Component, OnInit } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { DishService } from '../../../services/dish.service';
import { DishDTO } from '../../../dtos/dish.model';
import { ActivatedRoute, Router } from '@angular/router';

@Component({
  selector: 'app-dish-form',
  templateUrl: './dish-form.component.html',
  styleUrls: ['./dish-form.component.css']
})
export class DishFormComponent implements OnInit {
  dish!: DishDTO;
  dishForm!: FormGroup;
  isSubmitting = false;
  submitErrorMessage: string | null = null;
  editMode = false;
  dishId?: number;
  previewImage: string | ArrayBuffer | null = null;

  allAllergens = [
    { name: 'GLUTEN', imageUrl: '/assets/img/allergen_symbols/allergen_symbol_gluten.png' },
    { name: 'CRUSTACEANS', imageUrl: '/assets/img/allergen_symbols/allergen_symbol_crustacean.png' },
    { name: 'EGGS', imageUrl: '/assets/img/allergen_symbols/allergen_symbol_eggs.png' },
    { name: 'FISH', imageUrl: '/assets/img/allergen_symbols/allergen_symbol_fish.png' },
    { name: 'PEANUTS', imageUrl: '/assets/img/allergen_symbols/allergen_symbol_peanut.png' },
    { name: 'SOYBEANS', imageUrl: '/assets/img/allergen_symbols/allergen_symbol_soybeans.png' },
    { name: 'NUTS', imageUrl: '/assets/img/allergen_symbols/allergen_symbol_nuts.png' },
    { name: 'CELERY', imageUrl: '/assets/img/allergen_symbols/allergen_symbol_cereal.png' },
    { name: 'MUSTARD', imageUrl: '/assets/img/allergen_symbols/allergen_symbol_mustard.png' },
    { name: 'SESAME', imageUrl: '/assets/img/allergen_symbols/allergen_symbol_sesame.png' },
    { name: 'SULPHITES', imageUrl: '/assets/img/allergen_symbols/allergen_symbol_sulfites.png' },
    { name: 'MOLLUSCS', imageUrl: '/assets/img/allergen_symbols/allergen_symbol_mollusks.png' }
  ];

  constructor(
      private fb: FormBuilder,
      private dishService: DishService,
      private router: Router,
      private route: ActivatedRoute
  ) {}

  ngOnInit(): void {
    this.dishForm = this.fb.group({
      name: ['', Validators.required],
      description: [''],
      price: [0, [Validators.required, Validators.min(0)]],
      ingredients: ['', Validators.required],
      isVegan: [false],
      allergens: [[]],
      imageField: [null]
    });

    this.route.paramMap.subscribe(params => {
      const id = params.get('id');
      if (id) {
        this.editMode = true;
        this.dishId = +id;
        this.loadDish(this.dishId);
      }
    });
  }

  loadDish(id: number): void {
    this.dishService.getDishById(id).subscribe({
      next: (dish) => {
        this.dish = dish;
        this.dishForm.patchValue({
          ...dish,
          ingredients: dish.ingredients.join(', '),
          allergens: dish.allergens ?? []
        });
      },
      error: (err) => {
        console.error('Error loading dish:', err);
      }
    });
  }

  onSubmit(): void {
    if (this.dishForm.invalid) {
      this.dishForm.markAllAsTouched();
      return;
    }

    this.isSubmitting = true;
    this.submitErrorMessage = null;

    const formValues = this.dishForm.value;

    const dishData: DishDTO = {
      ...formValues,
      id: this.dishId ?? null,
      ingredients: formValues.ingredients.split(',').map((i: string) => i.trim()),
      allergens: formValues.allergens,
      dishImagePath: this.dish?.dishImagePath ?? '',
      // dishImageFile: this.dish?.dish ?? '',
      image: !!formValues.imageField,
      available: this.dish?.available ?? true,
      rates: this.dish?.rates ?? [],
      rate: this.dish?.rate ?? 0,
    };

    console.log(dishData);
    const request$ = this.editMode && this.dishId
        ? this.dishService.updateDish(this.dishId, dishData)
        : this.dishService.addDish(dishData);

    request$.subscribe({
      next: () => {
        this.isSubmitting = false;
        this.router.navigate(['/dishes']);
      },
      error: (err) => {
        this.isSubmitting = false;
        this.submitErrorMessage = err.error?.message || 'Error while saving the dish.';
      }
    });
  }

  onImageSelected(event: Event) {
    const file = (event.target as HTMLInputElement)?.files?.[0];
    if (file) {
      const reader = new FileReader();
      reader.onload = () => {
        this.previewImage = reader.result;
      };
      reader.readAsDataURL(file);
      this.dishForm.patchValue({ imageField: file });
    }
  }

  onAllergenChange(event: any) {
    const selectedAllergens: string[] = this.dishForm.value.allergens;
    const allergen = event.target.value;

    if (event.target.checked) {
      this.dishForm.patchValue({ allergens: [...selectedAllergens, allergen] });
    } else {
      this.dishForm.patchValue({
        allergens: selectedAllergens.filter(a => a !== allergen)
      });
    }
  }

  // Getters para validaciones en template
  get name() { return this.dishForm.get('name'); }
  get price() { return this.dishForm.get('price'); }
  get ingredients() { return this.dishForm.get('ingredients'); }
  get isVegan() { return this.dishForm.get('isVegan'); }
  get description() { return this.dishForm.get('description'); }
  get image() { return this.dishForm.get('image'); }
}
