using PizzeriaProject.Models.Datas;
using System;
using System.Collections.Generic;
using System.ComponentModel.DataAnnotations;
using System.ComponentModel.DataAnnotations.Schema;
using System.Linq;
using System.Threading.Tasks;

namespace PizzeriaProject.Models.Items
{
    public class PizzaIngredientItem
    {
        [Key]
        public int Id { set; get; }

        [Required(ErrorMessage = "Please provide quantity")]
        [Range(0, 10, ErrorMessage = "Ingredients quantity must be between 0 and 10")]
        public int Quantity { set; get; }

        [ForeignKey("PizzaIngredientData")]
        public int IngredientId { set; get; }

        [ForeignKey("PizzaItem")]
        public int? PizzaId { set; get; }

        public virtual PizzaIngredientData Ingredient { set; get; }
        public virtual PizzaItem Pizza { set; get; }

        [DisplayFormat(ApplyFormatInEditMode = true, DataFormatString = "{0:c}")]
        public virtual decimal Price
        {
            get
            {
                if (Ingredient != null)
                    return Quantity * Ingredient.Price;
                else return 0;
            }
        }
    }
}
