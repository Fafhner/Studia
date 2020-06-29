using System;
using Microsoft.EntityFrameworkCore.Migrations;

namespace PizzeriaProject.Migrations
{
    public partial class Update_v01 : Migration
    {
        protected override void Up(MigrationBuilder migrationBuilder)
        {
            migrationBuilder.UpdateData(
                table: "Ingriedients",
                keyColumn: "Id",
                keyValue: 1,
                column: "Price",
                value: 2.0m);

            migrationBuilder.UpdateData(
                table: "Ingriedients",
                keyColumn: "Id",
                keyValue: 3,
                column: "Name",
                value: "Chicken");

            migrationBuilder.InsertData(
                table: "Ingriedients",
                columns: new[] { "Id", "Name", "Price" },
                values: new object[,]
                {
                    { 5, "Sausage", 6.0m },
                    { 6, "Tomato sauce", 1.0m },
                    { 7, "Green pepper", 2.0m },
                    { 8, "Red pepper", 2.0m },
                    { 9, "Onion", 2.0m },
                    { 10, "Mushrooms", 2.0m }
                });

        }

        protected override void Down(MigrationBuilder migrationBuilder)
        {
            migrationBuilder.DeleteData(
                table: "Ingriedients",
                keyColumn: "Id",
                keyValue: 5);

            migrationBuilder.DeleteData(
                table: "Ingriedients",
                keyColumn: "Id",
                keyValue: 6);

            migrationBuilder.DeleteData(
                table: "Ingriedients",
                keyColumn: "Id",
                keyValue: 7);

            migrationBuilder.DeleteData(
                table: "Ingriedients",
                keyColumn: "Id",
                keyValue: 8);

            migrationBuilder.DeleteData(
                table: "Ingriedients",
                keyColumn: "Id",
                keyValue: 9);

            migrationBuilder.DeleteData(
                table: "Ingriedients",
                keyColumn: "Id",
                keyValue: 10);

            migrationBuilder.UpdateData(
                table: "Ingriedients",
                keyColumn: "Id",
                keyValue: 1,
                column: "Price",
                value: 3.0m);

            migrationBuilder.UpdateData(
                table: "Ingriedients",
                keyColumn: "Id",
                keyValue: 3,
                column: "Name",
                value: "Chcicken");

        }
    }
}
