using Microsoft.AspNetCore.Mvc;
using PizzeriaProject.Models.Datas;
using System;
using System.Collections.Generic;
using System.ComponentModel.DataAnnotations;
using System.ComponentModel.DataAnnotations.Schema;
using System.Linq;

namespace PizzeriaProject.Models.Items
{

    public class PizzaItem
    {
        public PizzaItem() {
            Ingredients = new List<PizzaIngredientItem>();
        }

        [Key]
        public int Id { set; get; }

        [StringLength(25, MinimumLength = 4, ErrorMessage = "Pizza name must be between 4 and 25 characters long.")]
        [Required(ErrorMessage = "Please add pizza name.")]
        [Remote(action: "VerifyNameExist", controller: "Pizzas")]
        public string Name { set; get; }


        [ForeignKey("PizzaSizeData")]
        public int? SizeId { set; get; }

        [ForeignKey("PizzaDoughData")]
        public int? DoughId { set; get; }

        [DataType(DataType.Date)]
        [DisplayFormat(DataFormatString = "{0:yyyy-MM-dd}", ApplyFormatInEditMode = true)]
        public DateTime Timestamp { set; get; }

        [DisplayFormat(ApplyFormatInEditMode = true, DataFormatString = "{0:c}")]
        public virtual decimal Price
        {
            get
            {
                if (Ingredients != null && Size != null && Dough != null)
                    return Size.Price + Dough.Price + Ingredients.Sum(i => i.Price);
                else
                    return 0;
            }
        }
        public virtual PizzaSizeData Size { set; get; }

        public virtual PizzaDoughData Dough { set; get; }

        public virtual IList<PizzaIngredientItem> Ingredients { set; get; }

    }
}
